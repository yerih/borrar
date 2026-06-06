package com.mivuelto.feature.purchase.ui.login

import android.content.Context
import android.os.Build
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.design.textfields.OutlinedTextFieldCustom
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import java.util.UUID

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClick: (usuario: String, contraseña: String) -> Unit = { _, _ -> },
    onBack: () -> Unit = {},

) {

    val user = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val userError = remember { mutableStateOf(false) }
    val passwordError = remember { mutableStateOf(false) }
    val appName = getOrCreateDeviceId(LocalContext.current)

    BackHandler{ onBack() }

    Box(modifier = Modifier.fillMaxSize()) {

        CorpoCreditLogo(modifier = Modifier.align(Alignment.TopCenter).padding(top = 10.dp))
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp).align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(text = "Bienvenido a CorpoServices", style = Lato.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))


            Text(
                text = stringResource(R.string.login)+" ${appName}",
                style = Lato.headlineSmall,
                modifier = Modifier.padding(top = 20.dp, bottom = 30.dp),
            )

            OutlinedTextFieldCustom(
                value = user.value,
                onValueChange = { user.value = it },
                label = "Usuario",
                isError = userError.value,
                errorMessage = "El usuario es requerido",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextFieldCustom(
                value = password.value,
                onValueChange = { password.value = it },
                label = "Contraseña",
                isError = passwordError.value,
                errorMessage = "La contraseña es requerida",
                isPassword = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    onLoginClick(user.value, password.value)
                })
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        ButtonFilled(
            text = stringResource(R.string.login),
            paddingHz = 30.dp,
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 40.dp),
        ) {
            userError.value = user.value.isBlank()
            passwordError.value = password.value.isBlank()

            if (!userError.value && !passwordError.value) {
                onLoginClick(user.value, password.value)
            }
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


fun getOrCreateDeviceId(context: Context): String {
    val prefs = context.getSharedPreferences("device_prefs", Context.MODE_PRIVATE)
    var deviceId = prefs.getString("device_id", null)

    if (deviceId == null) {
        deviceId = UUID.randomUUID().toString()
        prefs.edit().putString("device_id", deviceId).apply()
    }

    return deviceId
}
