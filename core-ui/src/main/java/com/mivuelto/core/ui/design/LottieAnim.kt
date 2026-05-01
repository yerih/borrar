package com.mivuelto.core.ui.design

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.clickableNoRipple

@Composable
fun LottieAnim(
    rawId: Int,
    iterations: Int = LottieConstants.IterateForever,
    modifier: Modifier,
    replayOnClick: Boolean = false,
    onClick: ()->Unit = {}
){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawId))
    var isPlaying by remember{mutableStateOf(replayOnClick)}
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        iterations = 1
    )

    if(replayOnClick) {
        LaunchedEffect(key1 = progress) {
            if (progress == 0f) isPlaying = true
            if (progress == 1f) isPlaying = false
        }

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier.clickableNoRipple{
                isPlaying = true
                onClick()
            },
        )
    }
    else{

        LottieAnimation(
            composition = composition,
            iterations = iterations,
            modifier = modifier.clickable{onClick()},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        LottieAnim(rawId = R.raw.loading, modifier = Modifier){}
    }
}

