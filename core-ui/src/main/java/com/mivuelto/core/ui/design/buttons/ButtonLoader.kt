package com.mivuelto.core.ui.design.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mivuelto.core.ui.theme.BluePrimary
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mediosdepago.corpocredit.core.ui.R
import com.mivuelto.core.ui.theme.isVisible

@Composable
fun ButtonLoader(
    text: String? = null,
    textId: Int = R.string.button_text,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
    modifier: Modifier = Modifier.fillMaxWidth(),
    isMaxWidth: Boolean = true,
    paddingHz: Dp = 90.dp,
//    isLoading: Boolean = true,
    onClick: ()->Unit,
){
    val elevation = 5.dp
    val fontSize = 18.sp
    Button(
        modifier = if(isMaxWidth)modifier
            .fillMaxWidth()
            .padding(horizontal = paddingHz)
        else modifier.padding(horizontal = paddingHz),
        colors = colors,
//        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = elevation,
            focusedElevation = elevation
        ),
        onClick = { if(enabled) onClick() }
    ){
        Box(contentAlignment = Alignment.Center){
            Text(
                text = text?.uppercase()?: stringResource(id = textId).uppercase(),
                fontSize = fontSize,
                color = Color.White,
                modifier = Modifier.padding(vertical = 5.dp).isVisible(enabled)
            )
            Text(
                text = stringResource(id = R.string.loading).uppercase(),
                fontSize = fontSize,
                color = Color.White,
                modifier = Modifier.padding(vertical = 5.dp).isVisible(!enabled)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        ButtonLoader{}
    }
}
