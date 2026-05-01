package com.mivuelto.core.ui.design

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.Lato
import com.mediosdepago.corpocredit.core.ui.R
import com.mivuelto.core.ui.navigation.NavFeature
import com.mivuelto.core.ui.design.logos.BankLogo
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.design.logos.CrediCardLogo

@Composable
fun HeaderAndFooterLogos(
    modifier: Modifier = Modifier,
    feature: NavFeature = NavFeature.HOME,
    subTitle: Int = 0,
    titleId: Int = 0,
    subTitPadTop: Dp = 0.dp,
    bankImg: suspend ()->Bitmap? = {null},
    content: @Composable (modifier: Modifier) -> Unit = {},
) {
    var bankLogo by remember{ mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(key1 = Unit){ bankLogo = bankImg() }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 5.dp, top = 5.dp),
    ) {
        val (corpocredit, contentRef, titles, credicard) = createRefs()
        val bottomGuide = createGuidelineFromTop(1f)
        CorpoCreditLogo(modifier = Modifier.constrainAs(corpocredit) {
            centerHorizontallyTo(parent)
        })
        if (feature != NavFeature.INVOICES)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.constrainAs(titles) {
                    centerHorizontallyTo(parent)
                    top.linkTo(corpocredit.bottom)
                }
            ) {
                if (feature != NavFeature.HOME || titleId != 0)
                    Text(
                        text = stringResource(id = getTitleByFeature(feature, titleId)),
                        style = Lato.screenTitle,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                if (subTitle != 0)
                    Text(
                        text = stringResource(id = subTitle),
                        style = Lato.screenSubTitle,
                        modifier = Modifier.padding(top = subTitPadTop)
                    )

            }
        content(modifier = Modifier.constrainAs(contentRef) {
            centerHorizontallyTo(parent)
            top.linkTo(titles.bottom)
//            top.linkTo(corpocredit.bottom)
            bottom.linkTo(credicard.top)
        })
        if(bankLogo != null) BankLogo(bankLogo, Modifier.constrainAs(credicard) {
            centerHorizontallyTo(parent)
            bottom.linkTo(bottomGuide)
        })
        else CrediCardLogo(modifier = Modifier.constrainAs(credicard) {
            centerHorizontallyTo(parent)
            bottom.linkTo(bottomGuide)
        })
    }
}


private fun getTitleByFeature(feature: NavFeature, titleId: Int = 0): Int =
    if (titleId != 0) titleId
    else when (feature) {
        NavFeature.PURCHASE -> R.string.purchase
        NavFeature.OTHER_OPERATIONS -> R.string.operations
        NavFeature.VOID -> R.string.void_
        NavFeature.SETTLEMENT -> R.string.settlement
        else -> R.string.title_not_valid
    }


@Preview(
    widthDp = 360,
    heightDp = 640,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun DefaultPreview() {
    CorpoCreditTheme {
        HeaderAndFooterLogos(
//            feature = NavFeature.INVOICES,
            feature = NavFeature.PURCHASE,
            subTitle = R.string.enter_id
        ) {}
    }
}

