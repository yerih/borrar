package com.mivuelto.core.ui.design.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mivuelto.core.ui.theme.BluePrimary
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.R

@Composable
fun ButtonFilled(
    text: String? = null,
    textId: Int = R.string.button_text,
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
    modifier: Modifier = Modifier.fillMaxWidth(),
    isMaxWidth: Boolean = true,
    paddingHz: Dp = 70.dp,
    onClick: ()->Unit,
){
    val elevation = 5.dp
    Button(
        modifier = if(isMaxWidth)modifier
            .fillMaxWidth()
            .padding(horizontal = paddingHz)
        else modifier.padding(horizontal = paddingHz),
        colors = colors,
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = 0.dp,
            disabledElevation = elevation,
            hoveredElevation = elevation,
            focusedElevation = elevation
        ),
        onClick = onClick
    ){
        Text(
            text = text?.uppercase()?: stringResource(id = textId).uppercase(),
            fontSize = 17.sp,
            color = Color.White,
            modifier = Modifier.padding(vertical = 5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        ButtonFilled(){}
    }
}
