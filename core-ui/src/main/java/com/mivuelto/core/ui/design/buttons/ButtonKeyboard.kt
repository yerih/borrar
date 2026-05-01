package com.mivuelto.core.ui.design.buttons

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.theme.BlueSecondary
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.GreenEnter
import com.mediosdepago.corpocredit.core.ui.R
import com.mivuelto.core.ui.theme.RedDelete
import com.mivuelto.core.ui.theme.Roboto

@Composable
fun ButtonKeyboard(
    id: Int = 0,
    modifier: Modifier = Modifier.size(70.dp, 44.dp),
    onClick: (Int)->Unit = {}
){
    when(id){
        10 -> Button(
                modifier = modifier,
                colors = ButtonDefaults.buttonColors(containerColor = RedDelete),
                shape = RoundedCornerShape(4.dp),
                onClick = {onClick(id)}
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize().padding(vertical = 5.dp, horizontal = 10.dp),//, horizontal = 20.dp),
                    contentDescription = "delete icon"
                )
            }

        11 -> Button(
                modifier = modifier,
                colors = ButtonDefaults.buttonColors(containerColor = BlueSecondary),
                shape = RoundedCornerShape(4.dp),
                onClick = {onClick(id)},
            ) {
                Text(text = "0", style = Roboto.buttonKeyboard)
            }
        12 -> Button(
                modifier = modifier,
                colors = ButtonDefaults.buttonColors(containerColor = GreenEnter),
                shape = RoundedCornerShape(4.dp),
                onClick = {onClick(id)}
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_enter),
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize().padding(vertical = 7.dp),
                    contentDescription = "delete icon"
                )
            }
        else -> Button(
                modifier = modifier,
                colors = ButtonDefaults.buttonColors(containerColor = BlueSecondary),
                shape = RoundedCornerShape(4.dp),
                onClick = {onClick(id)}
            ) {
                Text(text = "$id", style = Roboto.buttonKeyboard)
            }
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        ButtonKeyboard(){}
    }
}

@Preview(showBackground = true)
@Composable
private fun DeletePreview() {
    CorpoCreditTheme {
        ButtonKeyboard(10){}
    }
}
@Preview(showBackground = true)
@Composable
private fun DonePreview() {
    CorpoCreditTheme {
        ButtonKeyboard(12){}
    }
}
