package com.mivuelto.core.ui.design.logos

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mediosdepago.corpocredit.core.ui.R

@Composable
fun CorpoCreditLogo(
    modifier: Modifier = Modifier,//.scale(0.6f),//.size(175.dp, 57.dp),
    isVertical: Boolean = false
){
    Image(
        modifier = if(isVertical) modifier.size(150.dp, 80.dp) else modifier.size(175.dp, 47.dp),//modifier.size(190.dp, 57.dp),//.size(175.dp, 57.dp),
        contentScale = ContentScale.FillBounds,
//        contentScale = ContentScale.Crop,
        painter = painterResource(id = if(isVertical)R.drawable.logo_corpocredit_v else R.drawable.logo_corpocredit),
//        painter = painterResource(id = if(isVertical)R.drawable.logo_librepago else R.drawable.logo_librepago),
        contentDescription = "logo corpocredit"
    )
}

@Composable
fun CrediCardLogo(
    modifier: Modifier = Modifier
){
    Image(
        modifier = modifier.size(75.dp, 47.dp),//.size(95.dp, 67.dp),
        painter = painterResource(id = R.drawable.logo_credicard),
        contentDescription = "logo corpocredit"
    )
}

@Composable
fun BankLogo(bitmap: Bitmap? = null, modifier: Modifier = Modifier){
    Image(
        modifier = modifier.size(75.dp, 47.dp),//.size(95.dp, 67.dp),
        bitmap = bitmap?.asImageBitmap()?:ImageBitmap(75, 47),
        contentDescription = "logo banco"
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewCorpoVertical(){
    CorpoCreditLogo(isVertical = true)
}



@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DefaultPreviewCorpoCredit(){
    CorpoCreditLogo()
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DefaultPreviewCrediCard(){
    CrediCardLogo()
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewBankLogo(){
    BankLogo()
}

