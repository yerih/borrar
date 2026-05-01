package com.mivuelto.core.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mediosdepago.corpocredit.core.ui.R
import com.mivuelto.core.ui.design.HeaderAndFooter2
import com.mivuelto.core.ui.design.IconMsg
import com.mivuelto.core.ui.design.Loader
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow



@Composable
fun LoaderResponseScreen(
    eventFlow: Flow<UiEvent> = Channel<UiEvent>().receiveAsFlow(),
    onAbort: ()->Unit = {},
    onBack: ()->Unit = {},
    withButtonCancel: Boolean = false,
    bankImg: suspend ()-> Bitmap? = {null},
    headerTitle: Int = 0,
    hasProgress: Boolean = false,
    msgInitId: Int = R.string.processing,
    action: ()->Unit = {},
    onInvError: ()->Unit = {},
    onTaskDone: ()->Unit = {}
){
    var isLoading by remember{ mutableStateOf(true) }
    var isDone by remember{ mutableStateOf(false) }
    var isError by remember{ mutableStateOf(false) }
    var isBackToHome by remember{ mutableStateOf(true) }
    val context = LocalContext.current
    var msg by remember{ mutableStateOf(context.getString(msgInitId)) }
    var progress = remember{ mutableStateOf("0") }
    val hasProg = remember{ mutableStateOf(hasProgress) }
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = Unit){
        action()
        eventFlow.collect{ event ->
            isLoading = false; isError = false; isDone = false
            when(event){
                is UiEvent.Error -> {
                    isError = true
                    msg = event.msg.ifEmpty {
                        if(event.msgId != 0) context.getString(event.msgId) else ""
                    }
                    isBackToHome = event.isBackToHome
                }
                is UiEvent.Loader -> { isLoading = event.isLoading; msg = context.getString(event.msg) }
                is UiEvent.Notification -> { isLoading = event.isLoading; msg = event.msg }
                is UiEvent.TaskDone -> {
                    isDone = true
                    hasProg.value = false
                    msg = if(event.msgId == 0) event.msg
                    else context.getString(event.msgId)
                }
                UiEvent.OnBack -> onBack()
                is UiEvent.Progress -> {
                    isLoading = event.isLoading
                    hasProg.value = event.isLoading; progress.value = ""+event.progress
                }
                else -> Unit
            }
        }
    }


    BackHandler(onBack = { if(!isLoading) onAbort() })
    HeaderAndFooter2(
        subTitleId = headerTitle,
        subTitPadTop = 20.dp,
        isScrollable = false,
        bankImg = bankImg
    ){
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)) {
            val (loader, group, btn, progressRef) = createRefs()
            val centerGuide = createGuidelineFromTop(0.4f)
            val bottomGuide = createGuidelineFromTop(1f)

            Loader(
                isLoading = isLoading,
                msg = msg,
                hasProgress = hasProg,
                progress = progress,
                modifier = Modifier.constrainAs(loader){
                    centerHorizontallyTo(parent)
                    centerAround(centerGuide)
                }
            )
            Column(
                modifier = Modifier.constrainAs(group){
                    centerHorizontallyTo(parent)
                    centerAround(centerGuide)
                }.verticalScroll(scrollState)
            ){
                IconMsg(
                    isDone = isDone,
                    isError = isError,
                    msg = msg,
                )
            }
            if(isDone)
                ButtonFilled(
                    textId = R.string.continue_,
                    modifier = Modifier.constrainAs(btn) {
                            centerHorizontallyTo(parent)
                            bottom.linkTo(bottomGuide)
                        }
                        .padding(bottom = 20.dp),
                    onClick = { onTaskDone() }
                )
            else {
                if(isError && !withButtonCancel)
                    ButtonFilled(
                        textId = R.string.continue_,
                        modifier = Modifier.constrainAs(btn) {
                                centerHorizontallyTo(parent)
                                bottom.linkTo(bottomGuide)
                            }.padding(bottom = 20.dp),
                        onClick = if(isBackToHome) onBack else onInvError
                    )
                if(withButtonCancel)
                    ButtonFilled(
                        textId = R.string.cancel,
                        modifier = Modifier
                            .constrainAs(btn) {
                                centerHorizontallyTo(parent)
                                bottom.linkTo(bottomGuide)
                            }
                            .padding(bottom = 20.dp),
                        onClick = { msg = context.getString(R.string.canceling); onAbort() }
                    )
            }
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
        LoaderResponseScreen(headerTitle = R.string.load_tms_parameters)
    }
}


