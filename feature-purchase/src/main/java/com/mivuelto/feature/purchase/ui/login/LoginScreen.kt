package com.mivuelto.feature.purchase.ui.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.design.textfields.OutlinedTextFieldCustom
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.core.ui.theme.CorpoCreditTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
//    onLoginClick: (user: String, password: String) -> Unit = { _, _ -> },
    onBack: () -> Unit = {},
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val user = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val userError = remember { mutableStateOf(false) }
    val passwordError = remember { mutableStateOf(false) }


    BackHandler{ onBack() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToHome -> onLoginSuccess()
                is LoginEffect.ShowToast -> Unit
                is LoginEffect.TextFieldErrors -> {
                    userError.value = effect.userError
                    passwordError.value = effect.passwordError
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        CorpoCreditLogo(modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 10.dp))
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(text = "Bienvenido a CorpoServices", style = Lato.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))


            Text(
                text = stringResource(R.string.login)+"\nserial: ${viewModel.serialNum}",
                style = Lato.headlineSmall,
                modifier = Modifier.padding(top = 20.dp, bottom = 30.dp),
            )

            OutlinedTextFieldCustom(
                value = state.user,
                onValueChange = {viewModel.onIntent(LoginIntent.OnUsernameChanged(it))},//{ user.value = it },
                label = "Usuario",
                isError = userError.value,
                errorMessage = "El usuario es requerido",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextFieldCustom(
                value = state.password,//password.value,
                onValueChange = {viewModel.onIntent(LoginIntent.OnPasswordChanged(it))},//{ user.value = it },
                label = "Contraseña",
                isError = passwordError.value,
                errorMessage = "La contraseña es requerida",
                isPassword = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.onIntent(intent = LoginIntent.OnLoginClicked)
                })
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        ButtonFilled(
            text = stringResource(R.string.login),
            paddingHz = 60.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp),
        ) {
            viewModel.onIntent(LoginIntent.OnLoginClicked)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    CorpoCreditTheme {
        LoginScreen()
    }
}


