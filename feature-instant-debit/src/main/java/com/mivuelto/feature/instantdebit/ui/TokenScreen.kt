package com.mivuelto.feature.instantdebit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.BaseScreen
import com.mivuelto.core.ui.design.HeaderTitles
import com.mivuelto.core.ui.design.buttons.ButtonBorder
import com.mivuelto.core.ui.design.buttons.ButtonLoader
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.design.textfields.TextFieldCustom
import com.mivuelto.feature.instantdebit.navigation.InstantDebitViewModel
import kotlinx.coroutines.delay

@Composable
fun TokenScreen(
    viewModel: InstantDebitViewModel,
    flowTitle: String,
    onBack: () -> Unit,
    onCharge: () -> Unit
) {
    var token by remember { mutableStateOf("") }
    val tokenError = remember { mutableStateOf(false) }
    val tokenResult = viewModel.tokenResult
    var secondsLeft by remember { mutableStateOf(tokenResult?.expiresInSeconds ?: 0) }
    val expired = tokenResult != null && secondsLeft <= 0

    LaunchedEffect(tokenResult) {
        val expires = tokenResult?.expiresInSeconds ?: return@LaunchedEffect
        token = ""
        tokenError.value = false
        secondsLeft = expires
        while (secondsLeft > 0) {
            delay(1_000)
            secondsLeft--
        }
    }

    val onDone: () -> Unit = {
        if (!expired) {
            tokenError.value = token.length != TOKEN_LENGTH
            if (!tokenError.value) {
                viewModel.startCharge(token)
                onCharge()
            }
        }
    }

    BaseScreen(onBack = onBack) {
        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp)) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CorpoCreditLogo(modifier = Modifier.padding(top = 10.dp))
                HeaderTitles(
                    flowTitle = flowTitle,
                    instruction = "Ingrese token"
                )
                if (tokenResult != null) {
                    Text(
                        text = if (expired) "Tiempo expirado"
                        else "%02d:%02d".format(secondsLeft / 60, secondsLeft % 60),
                        color = if (expired) Color.Red else Color.Black,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                viewModel.tokenErrorMsg?.let { msg ->
                    Text(
                        text = msg,
                        color = Color.Red,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, start = 20.dp, end = 20.dp)
                    )
                }
            }

            TextFieldCustom(
                value = token,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }
                    if (digits.length <= TOKEN_LENGTH) {
                        token = digits
                        tokenError.value = false
                    }
                },
                label = "Token",
                isError = tokenError.value,
                errorMessage = "El token debe tener $TOKEN_LENGTH dígitos",
                textStyle = LocalTextStyle.current.copy(fontSize = 37.sp, textAlign = TextAlign.Center),
                modifier = Modifier.align(Alignment.Center).padding(horizontal = 10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onDone() })
            )

            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                ButtonBorder(
                    modifier = Modifier.weight(1f),
                    paddingHz = 0.dp,
                    text = stringResource(R.string.cancel),
                    onClick = onBack
                )

                Spacer(modifier = Modifier.width(10.dp))

                if (expired) {
                    ButtonLoader(
                        text = stringResource(R.string.retry),
                        isLoading = viewModel.isRequestingToken,
                        isMaxWidth = false,
                        paddingHz = 0.dp,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.retryToken() }
                    )
                } else {
                    ButtonLoader(
                        text = stringResource(R.string.next),
                        isMaxWidth = false,
                        paddingHz = 0.dp,
                        modifier = Modifier.weight(1f),
                        onClick = { onDone() }
                    )
                }
            }
        }
    }
}

private const val TOKEN_LENGTH = 8