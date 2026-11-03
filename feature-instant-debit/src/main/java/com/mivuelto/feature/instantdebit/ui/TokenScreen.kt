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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.BaseScreen
import com.mivuelto.core.ui.design.HeaderTitles
import com.mivuelto.core.ui.design.buttons.ButtonBorder
import com.mivuelto.core.ui.design.buttons.ButtonLoader
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.design.textfields.TextFieldCustom
import com.mivuelto.feature.instantdebit.navigation.InstantDebitViewModel

@Composable
fun TokenScreen(
    viewModel: InstantDebitViewModel,
    flowTitle: String,
    onBack: () -> Unit,
    onTaskDone: () -> Unit,
    onError: () -> Unit
) {
    var token by remember { mutableStateOf("") }
    val tokenError = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { event ->
            when (event) {
                is UiEvent.TaskDone -> onTaskDone()
                is UiEvent.Error -> onError()
                else -> Unit
            }
        }
    }

    val onDone: () -> Unit = {
        tokenError.value = token.length != TOKEN_LENGTH
        if (!tokenError.value) {
            viewModel.chargePayment(token)
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
                textStyle = LocalTextStyle.current.copy(fontSize = 24.sp, textAlign = TextAlign.Center),
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

                ButtonLoader(
                    text = stringResource(R.string.next),
                    isLoading = viewModel.isCharging,
                    isMaxWidth = false,
                    paddingHz = 0.dp,
                    modifier = Modifier.weight(1f),
                    onClick = { onDone() }
                )
            }
        }
    }
}

private const val TOKEN_LENGTH = 8