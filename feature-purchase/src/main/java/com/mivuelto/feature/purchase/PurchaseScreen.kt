package com.mivuelto.feature.purchase

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.sp
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.buttons.ButtonBorder
import com.mivuelto.core.ui.theme.Lato

@Composable
fun PurchaseScreen(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Pantalla de Compra", style = Lato.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        ButtonFilled(
            text = "Confirmar Compra",
            onClick = { /* Acción */ }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ButtonBorder(
            text = "Cancelar",
            onClick = onNavigateBack
        )
    }
}
