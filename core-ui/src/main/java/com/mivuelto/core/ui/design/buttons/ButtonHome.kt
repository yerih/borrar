package com.mivuelto.core.ui.design.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.theme.BlueSecondary
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.Lato
import com.mediosdepago.corpocredit.core.ui.R

@Composable
fun ButtonHome(
    text: String? = null,
    textId: Int = R.string.resumen_de_op,
    idIcon: Int = R.drawable.ic_resumen_operaciones,
    modifier: Modifier,
    onCLick: ()->Unit,
){
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = BlueSecondary),
        shape = RoundedCornerShape(28.dp),
        onClick = onCLick
    ){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = idIcon),
                contentDescription = "icono resumen de operaciones",
                modifier = Modifier.size(42.dp, 49.dp),
                tint = Color.White,
            )
            Text(
                text = text?.uppercase()?: stringResource(id = textId),
                textAlign = TextAlign.Center,
                style = Lato.buttonHome,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview//( widthDp = 102, heightDp = 98)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        ButtonHome(modifier = Modifier.size(150.dp, 130.dp)){}
    }
}
