package com.mivuelto.core.ui.design

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.core.ui.R

@SuppressLint("SuspiciousIndentation")
@Composable
fun IconMsg(
    modifier: Modifier = Modifier,
    isDone: Boolean = false,
    isError: Boolean = false,
    msg: String = "",
){

    if(isDone || isError)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ){
        LottieAnim(
            rawId = if (isDone) R.raw.success else if(isError) R.raw.failed else R.raw.loading,
            iterations = 1,
            replayOnClick = true,
            modifier = Modifier.size(150.dp, 150.dp)
        )
        Text(
            text = msg,
            style = getStyleText(isDone),
            modifier = Modifier.padding(horizontal = 30.dp)
        )
    }
}

private fun getStyleText(isSuccess: Boolean): TextStyle = if(isSuccess) Lato.headlineLarge else Lato.errorMsg




@Preview(
    widthDp = 420,
    heightDp = 680,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        IconMsg(isDone = true, msg = "Cargando...", )
    }
}
