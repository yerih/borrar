package com.mivuelto.core.ui.design

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.Lato
import com.mivuelto.core.ui.isVisible
import com.mivuelto.core.ui.navigation.NavFeature
import com.mivuelto.core.ui.design.logos.BankLogo
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.design.logos.CrediCardLogo
import com.mivuelto.core.ui.design.snackbar.SnackBarHost


@Composable
fun HeaderAndFooter2(
    modifier: Modifier = Modifier,
    feature: NavFeature = NavFeature.HOME,
    subTitleId: Int = 0,
    titleId: Int = 0,
    subTitPadTop: Dp = 0.dp,
    isScrollable: Boolean = true,
    bankImg: suspend ()->Bitmap? = {null},
    snackBarState: SnackbarHostState = remember{ SnackbarHostState()},
    content: @Composable (modifier: Modifier) -> Unit = {},
) {
    val scrollState = rememberScrollState()
    var bankLogo by remember{ mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(key1 = Unit){ bankLogo = bankImg() }

    Scaffold(
        topBar = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        CorpoCreditLogo(modifier = Modifier.padding(top = 5.dp))
                        if (feature != NavFeature.INVOICES)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                            ) {
                                Text(
                                    text = stringResource(id = getTitleByFeature(feature, titleId)),
                                    style = Lato.screenTitle,
                                    modifier = Modifier.isVisible(feature != NavFeature.HOME || titleId != 0)
                                )
                                if (subTitleId != 0)
                                    Text(
                                        text = stringResource(id = subTitleId),
                                        style = Lato.screenSubTitle,
                                        modifier = Modifier.padding(top = subTitPadTop, bottom = 5.dp)
                                    )

                            }
                    }
        },
        snackbarHost = { SnackBarHost(hostState = snackBarState)},
        bottomBar = { Box(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp), contentAlignment = Alignment.Center){
            if(bankLogo != null) BankLogo(bankLogo, Modifier)
            else CrediCardLogo(modifier = Modifier)
        } },
    ){
        ConstraintLayout(modifier = if(isScrollable) Modifier
            .padding(it)
            .verticalScroll(scrollState)
            else Modifier.padding(it)
        ) {
            val (contentRef, ) = createRefs()
            content(Modifier.constrainAs(contentRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(parent)
            })
        }
    }
}


private fun getTitleByFeature(feature: NavFeature, titleId: Int = 0): Int =
    if (titleId != 0) titleId
    else when (feature) {
        NavFeature.PURCHASE -> R.string.purchase
        NavFeature.OTHER_OPERATIONS -> R.string.operations
        NavFeature.VOID -> R.string.void_
        NavFeature.REP_SUMMARY -> R.string.operations_summary
        NavFeature.SETTINGS -> R.string.settings
        NavFeature.SETTLEMENT -> R.string.batch_settlement
        NavFeature.KEYS_DOWNLOADING -> R.string.keys_downloading
        else -> R.string.title_not_valid
    }



@Preview(
    widthDp = 360,
    heightDp = 620,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun MF919Preview() {
    CorpoCreditTheme {
        HeaderAndFooter2 (feature = NavFeature.PURCHASE, subTitleId = R.string.enter_id){}
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
        HeaderAndFooter2 (feature = NavFeature.PURCHASE, subTitleId = R.string.enter_id){}
    }
}