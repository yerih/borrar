@file:OptIn(ExperimentalComposeUiApi::class)

package com.mivuelto.core.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.launch
import com.mivuelto.core.ui.NavCommand
import com.mivuelto.core.ui.navigation.NavFeature
import com.mivuelto.core.ui.toast
import com.mivuelto.core.ui.design.HeaderAndFooter2
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.keyboard.Keyboard
import com.mivuelto.core.ui.design.snackbar.show
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

@ExperimentalComposeUiApi
@Composable
fun AccessScreen(
    modifier: Modifier = Modifier,
    destiny: NavFeature = NavFeature.HOME,
    route: String = "",
    password: String = "123456789",
    onCancelClicked: () -> Unit = {},
    onBack: () -> Unit = {},
    bankImg: suspend ()-> Bitmap? = {null},
    onAccessGranted: (String) -> Unit = {_->},
    eventFlow: Flow<UiEvent> = Channel<UiEvent>().receiveAsFlow(),

    ) {
    val context = LocalContext.current
    val toast: (Int)->Unit = { msgId -> context.toast(msgId) }
    val snackBarState = remember{ SnackbarHostState()}
    var isBack by remember{ mutableStateOf(false) }

    LaunchedEffect(key1 = Unit){ eventFlow.collect{event ->
        when(event){
            UiEvent.OnBack -> if(!isBack) {
                onBack()
                isBack = true
            }
            else -> Unit
        }
    } }

    HeaderAndFooter2(
        feature = destiny,
        subTitleId = R.string.enter_password,
        snackBarState = snackBarState,
        isScrollable = false,
        bankImg = bankImg
    ){
        ConstraintLayout(modifier = modifier.fillMaxHeight()
            .padding(horizontal = 20.dp)) {
            val (keyboard, button) = createRefs()

            Keyboard(
                toCompare = password,
                modifier = Modifier.constrainAs(keyboard) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                    bottom.linkTo(button.top, 5.dp)
                }
            ) { isGranted, _ ->
                if (isGranted) {
                    onAccessGranted(
                        route.ifEmpty { NavCommand.TypeSubRoute(destiny, destiny.route).route }
                    )
                }
                else
                    launch { snackBarState.show(context.getString(R.string.invalid_password)) }
            }

            ButtonFilled(
                textId = R.string.cancel,
                onClick = onCancelClicked,
                modifier = Modifier.constrainAs(button) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom)
                }
            )
        }

    }
}



@Preview(
    widthDp = 340,
    heightDp = 580,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        AccessScreen(
            modifier = Modifier,
            destiny = NavFeature.HOME,
            onCancelClicked = {},
            onAccessGranted = {}
        )
    }
}


