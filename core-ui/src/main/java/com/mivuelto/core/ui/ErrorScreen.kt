package com.mivuelto.core.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.constraintlayout.compose.Dimension
import com.mivuelto.core.ui.design.HeaderAndFooter2
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.core.ui.theme.RedDelete


@Composable
fun ErrorScreen(
    text: String = "",
    textId: Int = 0,
    code: Int = 0,
    feature: NavFeature = NavFeature.CHECK_PAYMENT,
    bankImg: suspend ()-> Bitmap? = {null},
    withRetryBtn: Boolean = true,
    onBack: ()->Unit = {},
    onRetryClicked: ()->Unit = {},
){
    BaseScreen(onBack = onBack) {
        HeaderAndFooter2(feature = feature, isScrollable = false, bankImg = bankImg){
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 20.dp)
            ) {
                val (btn, body) = createRefs()
                Column(
                    modifier = Modifier.constrainAs(body) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                        bottom.linkTo(btn.top, 16.dp)
                        height = Dimension.fillToConstraints
                    }
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
                ){
                    Icon(
                        painter = iconByCode(code),
                        tint = RedDelete,
                        modifier = Modifier.size(90.dp, 90.dp),
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
                ButtonFilled(
                    textId = if (withRetryBtn) R.string.retry else R.string.back,
                    modifier = Modifier.constrainAs(btn){
                        centerHorizontallyTo(parent)
                        bottom.linkTo(parent.bottom, 10.dp)
                    },
                    onClick = if (withRetryBtn) onRetryClicked else onBack
                )
            }
        }
    }

}

@Composable
fun textByCode(code: Int): String = "code not recognized$code"

@Composable
fun iconByCode(code: Int) = painterResource(id = R.drawable.ic_failed)


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

@Preview(
    widthDp = 420,
    heightDp = 680,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun LongMessagePreview() {
    CorpoCreditTheme {
        ErrorScreen(
            text = "No pudimos verificar el pago. El servicio de consultas no respondió a tiempo. " +
                "Verifique su conexión e intente nuevamente. Si el problema persiste, comuníquese " +
                "con soporte indicando el código de error y la fecha/hora de este intento. " +
                "Tenga a la mano el número de referencia de la operación.",
            withRetryBtn = false
        )
    }
}
