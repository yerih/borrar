package com.mivuelto.core.ui.design.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.theme.BluePrimary
import com.mivuelto.core.ui.theme.CorpoCreditTheme

@Composable
fun ButtonLoader(
    text: String? = null,
    textId: Int = R.string.button_text,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = BluePrimary,
        disabledContainerColor = BluePrimary
    ),
    modifier: Modifier = Modifier.fillMaxWidth(),
    isMaxWidth: Boolean = true,
    paddingHz: Dp = 90.dp,
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
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = elevation,
            focusedElevation = elevation
        ),
        onClick = onClick
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            if(isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text?.uppercase()?: stringResource(id = textId).uppercase(),
                fontSize = fontSize,
                color = Color.White,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        ButtonLoader(){}
    }
}
