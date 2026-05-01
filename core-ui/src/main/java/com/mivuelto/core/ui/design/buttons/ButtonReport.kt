package com.mivuelto.core.ui.design.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mediosdepago.core.addCommas
import com.mediosdepago.core.domain.CardBrandEnum
import com.mediosdepago.core.domain.DetailedReportModel
import com.mediosdepago.core.domain.LotType
import com.mediosdepago.core.formatDate
import com.mediosdepago.core.isOdd
import com.mediosdepago.core.toMaskedRange
import com.mivuelto.core.ui.theme.CorpoCreditTheme
import com.mivuelto.core.ui.theme.GreyDark
import com.mivuelto.core.ui.theme.GreyLight
import com.mivuelto.core.ui.theme.Lato
import com.mediosdepago.corpocredit.core.ui.R
import com.mivuelto.core.ui.design.shimmer


@Composable
fun ButtonReport(
    index: Int = 0,
    data: DetailedReportModel = DetailedReportModel(),
    modifier: Modifier = Modifier,
    shimmer: Boolean = false,
    onClick: (Int)->Unit = {},
){
    Row(modifier = modifier.fillMaxWidth().clickable { onClick(index) }
        .background(if (index.isOdd()) GreyDark else GreyLight)
        .shimmer(shimmer, 0.dp,)
        .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column(){
            Text(
                text = "${if(data.isSale)"Compra" else "Anulación"} - ${data.brand}",//,"${if(data.isSale)"Compra" else "Anulación"} - ${brandByLot(data.lotType, data.brand)}",
                style = Lato.invoiceAddress,
            )
            Text(
                text = data.reference,//data.recoveryRefNum,
                style = Lato.invoiceAddress,
            )
            Text(
                text = stringResource(id = if(data.isApproved)R.string.approved else R.string.declined),
                style = Lato.invoiceAddress,
            )
        }
        Text(
            text = "_",
            style = Lato.invoiceAddress,
        )
        Column(
            horizontalAlignment = Alignment.End
        ){

            Text(
                text = data.pan.toMaskedRange(end = 5),
                style = Lato.detail,
            )
            Text(
                text = data.date.formatDate(),
                style = Lato.detail,
            )
            Text(
                text = "Bs. ${data.amount.addCommas()}",
                style = Lato.detail,
            )
        }
    }
}

private fun brandByLot(lot: LotType, brand: CardBrandEnum) = if(lot != LotType.VISAMASTER_DEBIT)brand.name//lot.nameLot
    else when(brand){
        CardBrandEnum.VISA -> "VISA"
        CardBrandEnum.MASTERCARD -> "Mastercard"
        else -> ""
    }



@Preview(widthDp = 320, showBackground = true)
@Composable
private fun DefaultPreviewOdd() {
    CorpoCreditTheme { ButtonReport(index = 1) }
}


@Preview(widthDp = 320, showBackground = true)
@Composable
private fun DefaultPreviewEven() {
    CorpoCreditTheme { ButtonReport(index = 2, data = DetailedReportModel(isSale = false)) }
}
