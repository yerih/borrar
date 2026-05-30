package com.mivuelto.core.ui.design

import android.graphics.Bitmap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.NavFeature
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
        content(Modifier.constrainAs(contentRef) {
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
            feature = NavFeature.CHECK_PAYMENT,
            subTitle = R.string.enter_id
        ) {}
    }
}

