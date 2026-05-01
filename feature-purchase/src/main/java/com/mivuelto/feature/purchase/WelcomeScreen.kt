package com.mivuelto.feature.purchase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.theme.Lato

@Composable
fun WelcomeScreen(
    onNavigateToPurchase: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CorpoCreditLogo(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp)
        )
        
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Bienvenido a Mi Vuelto", style = Lato.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))

            ButtonFilled(
                text = "Ir a Comprar",
                onClick = onNavigateToPurchase
            )
        }
    }
}
