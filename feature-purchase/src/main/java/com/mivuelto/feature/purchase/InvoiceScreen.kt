package com.mivuelto.feature.purchase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.BaseScreen
import com.mivuelto.core.ui.design.buttons.ButtonBorder
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.theme.Lato


@Composable
fun InvoiceScreen(
    onBack: () -> Unit = {}
) {
    BaseScreen(onBack) {
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
                Text(text = stringResource(R.string.invoice), style = Lato.headlineMedium)
            }

            ButtonBorder(
                modifier = Modifier.align(Alignment.BottomCenter).padding(vertical = 20.dp),
                text = stringResource(com.mivuelto.core.ui.R.string.exit),
                onClick = onBack
            )
        }
    }
}


