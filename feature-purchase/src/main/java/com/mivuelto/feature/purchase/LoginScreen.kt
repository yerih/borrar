package com.mivuelto.feature.purchase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.design.HeaderAndFooter2
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.textfields.OutlinedTextFieldCustom
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.core.ui.theme.CorpoCreditTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClick: (usuario: String, contraseña: String) -> Unit = { _, _ -> },
    onBack: () -> Unit = {},
) {
    val usuario = remember { mutableStateOf("") }
    val contraseña = remember { mutableStateOf("") }
    val usuarioError = remember { mutableStateOf(false) }
    val contraseñaError = remember { mutableStateOf(false) }

    HeaderAndFooter2(isScrollable = false) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Inicio de sesión",
                style = Lato.headlineSmall,
                modifier = Modifier.padding(top = 20.dp, bottom = 30.dp),
            )

            OutlinedTextFieldCustom(
                value = usuario.value,
                onValueChange = { usuario.value = it },
                label = "Usuario",
                isError = usuarioError.value,
                errorMessage = "El usuario es requerido",
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextFieldCustom(
                value = contraseña.value,
                onValueChange = { contraseña.value = it },
                label = "Contraseña",
                isError = contraseñaError.value,
                errorMessage = "La contraseña es requerida",
                isPassword = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(24.dp))

            ButtonFilled(
                text = "Inicio de sesión",
                modifier = Modifier.fillMaxWidth(),
            ) {
                usuarioError.value = usuario.value.isBlank()
                contraseñaError.value = contraseña.value.isBlank()

                if (!usuarioError.value && !contraseñaError.value) {
                    onLoginClick(usuario.value, contraseña.value)
                }
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
