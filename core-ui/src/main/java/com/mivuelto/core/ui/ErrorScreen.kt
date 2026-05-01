package com.mivuelto.core.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.core.ui.navigation.NavFeature
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.theme.RedDelete
import com.mivuelto.core.ui.design.HeaderAndFooter2


@Composable
fun ErrorScreen(
    text: String = "",
    textId: Int = 0,
    code: Int = 0,
    feature: NavFeature = NavFeature.PURCHASE,
    bankImg: suspend ()-> Bitmap? = {null},
    withRetryBtn: Boolean = true,
    onBack: ()->Unit = {},
    onRetryClicked: ()->Unit = {},
){
    BackHandler(onBack = onBack)
    HeaderAndFooter2(feature = feature, isScrollable = false, bankImg = bankImg){
        ConstraintLayout(modifier = Modifier.fillMaxHeight().padding(horizontal = 20.dp)) {
            val (btn, body) = createRefs()
            Column(
                modifier = Modifier.constrainAs(body) {
                    centerHorizontallyTo(parent)
                    centerTo(parent)
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ){
                Icon(
                    painter = iconByCode(code),
                    tint = RedDelete,
                    modifier = Modifier.size(90.dp, 90.dp).takeIf { code != ErrorNum.CardNotRead.code }?:Modifier,
                    contentDescription = "icon"
                )
                Text(
                    text = when{
                        textId != 0 -> stringResource(id = textId)
                        code != 0 -> textByCode(code)
                        else -> text
                    },
                    style = Lato.errorTitle
                )
            }
            if(withRetryBtn)
            ButtonFilled(
                textId = R.string.retry,
                modifier = Modifier.constrainAs(btn){
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom, 10.dp)
                },
                onClick = onRetryClicked
            )
            else
                ButtonFilled(
                    textId = R.string.back,
                    modifier = Modifier.constrainAs(btn){
                        centerHorizontallyTo(parent)
                        bottom.linkTo(parent.bottom, 10.dp)
                    },
                    onClick = onBack
                )
        }
    }

}

@Composable
fun textByCode(code: Int): String = when(code){
    ErrorNum.NoError.code -> ""
    ErrorNum.CardNotRead.code -> stringResource(id = R.string.error_card_not_detected)
    ErrorNum.NotConnection.code -> stringResource(id = R.string.check_connection)
    ErrorNum.NoActiveLot.code -> stringResource(id = R.string.no_active_lot)
    ErrorNum.NoBinRange.code -> stringResource(id = R.string.no_bin_range)
    ErrorNum.NFCDisabled.code -> stringResource(id = R.string.nfc_is_not_enabled)
    else -> CupException.getErrMsg(""+code)//"code not recognized"
}

@Composable
fun iconByCode(code: Int) = when(code){
    ErrorNum.CardNotRead.code, ErrorNum.NFCDisabled.code -> painterResource(id = R.drawable.ic_card_not_read)
    else -> painterResource(id = R.drawable.ic_failed)
}


@Preview(
    widthDp = 420,
    heightDp = 680,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme { ErrorScreen(text = "Error message here!") }
}




