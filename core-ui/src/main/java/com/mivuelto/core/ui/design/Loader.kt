package com.mivuelto.core.ui.design

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mivuelto.core.ui.theme.Lato
import com.mediosdepago.corpocredit.core.ui.R
import com.mivuelto.core.ui.theme.isInvisible

@Composable
fun Loader(
    isLoading: Boolean = false,
    msg: String = "",
    modifier: Modifier = Modifier,
    hasProgress: MutableState<Boolean> = remember { mutableStateOf(false) },
    progress: MutableState<String> = remember { mutableStateOf("0") },//String = "",
){
    ConstraintLayout(modifier.isInvisible(isLoading)){
        val (anm, msgRef, progressRef) = createRefs()
        LottieAnim(
            rawId = R.raw.loading,
            modifier = Modifier.constrainAs(anm) { centerHorizontallyTo(parent) }
        )
        Text(
            text = msg,
            modifier = Modifier.constrainAs(msgRef) {
                centerHorizontallyTo(parent)
                top.linkTo(anm.bottom)
            }.padding(horizontal = 30.dp),
            style = Lato.screenTitle.copy(textAlign = TextAlign.Center)
        )

        if(hasProgress.value)
            Text(
                text = "% ${progress.value}",
                style = Lato.screenTitle.copy(textAlign = TextAlign.Center, fontSize = 15.sp),
                modifier = Modifier.constrainAs(progressRef){
                    centerHorizontallyTo(parent)
                    top.linkTo(msgRef.bottom, 10.dp)
                }.padding(top = 10.dp)
            )
    }
}


@Preview(
    widthDp = 360,
    heightDp = 680,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun DefaultPreview() {
    Loader(true, "Enviando operaciones pendientes (10/10)...", Modifier.fillMaxSize())
}
