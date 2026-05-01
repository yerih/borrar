package com.mivuelto.core.ui

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.core.ui.design.HeaderAndFooter2
import com.mivuelto.core.ui.design.Loader
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow


@Composable
fun LoaderScreen(
    eventFlow: Flow<UiEvent> = Channel<UiEvent>().receiveAsFlow(),
    bankImg: suspend ()-> Bitmap? = {null},
    onAbort: ()->Unit = {},
    onBack: ()->Unit = {},
    headerTitle: Int = 0,
    msgInitId: Int = R.string.processing,
    action: ()->Unit = {},
    onTaskDone: ()->Unit = {}
){
    var isLoading by remember{ mutableStateOf(true) }
    var isDone by remember{ mutableStateOf(false) }
    var isError by remember{ mutableStateOf(false) }
    val context = LocalContext.current
    var msg by remember{ mutableStateOf(context.getString(msgInitId)) }
    var loaderMsg by remember{ mutableStateOf(context.getString(R.string.loading)) }

    LaunchedEffect(key1 = Unit){
        action()
        eventFlow.collect{ event ->
            isLoading = false; isError = false; isDone = false
            when(event){
                is UiEvent.Loader -> { isLoading = event.isLoading; loaderMsg = context.getString(event.msg) }
                is UiEvent.Error -> { isError = true; msg = event.msg.ifEmpty { context.getString(event.msgId) } }
                is UiEvent.TaskDone -> {isDone = true; onTaskDone()}
                is UiEvent.Notification -> { isLoading = event.isLoading; loaderMsg = event.msg }
                UiEvent.OnBack -> onBack()
                else -> Unit
            }
        }
    }


    BackHandler(onBack = onAbort)
    HeaderAndFooter2(
        subTitleId = headerTitle,
        isScrollable = false,
        bankImg = bankImg
    ){
        ConstraintLayout(modifier = Modifier.fillMaxSize().padding(top = 20.dp).padding(horizontal = 20.dp)) {
            val (loader, msgRef, btn) = createRefs()
            val centerGuide = createGuidelineFromTop(0.4f)
            val bottomGuide = createGuidelineFromTop(1f)

            Loader(
                isLoading = isLoading,
                msg = msg,
                modifier = Modifier.constrainAs(loader){
                    centerHorizontallyTo(parent)
                    centerAround(centerGuide)
                }
            )
            if(isDone || isError)
                Text(text = msg, style = Lato.headlineMedium, textAlign = TextAlign.Center, modifier = Modifier.constrainAs(msgRef){
                    centerTo(parent)
                })
            ButtonFilled(
                textId = R.string.cancel,
                modifier = Modifier
                    .constrainAs(btn) {
                        centerHorizontallyTo(parent)
                        bottom.linkTo(bottomGuide)
                    }
                    .padding(bottom = 20.dp),
                onClick = { onAbort() }
            )
        }
    }
}


@Preview(
    widthDp = 420,
    heightDp = 680,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        LoaderScreen()
    }
}
