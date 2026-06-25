package com.mivuelto.feature.purchase.ui.invoices

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mivuelto.core.ui.R
import com.mivuelto.core.ui.ifThenDo
import com.mivuelto.core.ui.theme.Lato


@Composable
fun InvoiceElement(
    label: String,
    value: String,
    style: TextStyle = Lato.invoiceLabel(true),
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp),
    withoutFontWeight: Boolean = true,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ){
        Text(
            text = "$label:".uppercase(),
            style = style.copy(textAlign = TextAlign.Start),
            modifier = Modifier.wrapContentWidth()
        )
        Text(
            text = value,
            style = style.copy(textAlign = TextAlign.End),
            modifier = Modifier.wrapContentWidth()
        )
    }
}

@Composable
fun InvoiceElement(
    center: String,
    style: TextStyle = Lato.invoiceMerchant,
    isUppercase: Boolean = true,
) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
    ) {
        val (centerRef) = createRefs()
        Text(
            text = center.ifThenDo(isUppercase) { it.uppercase() },//if(isUppercase)"$center:".uppercase() else "$center:",
            style = style,
            modifier = Modifier.constrainAs(centerRef) {
                centerHorizontallyTo(parent)
            }
        )
    }
}

@Composable
fun InvoiceIconTitle(textId: Int, isSuccessIcon: Boolean = true, paddingStart: Dp = 0.dp, modifier: Modifier = Modifier,) {
    ConstraintLayout(
        modifier = modifier.padding(start = paddingStart).fillMaxWidth()
    ) {
        val (image, text) = createRefs()
        Image(
            painter = painterResource(id = if(isSuccessIcon) R.drawable.ic_success else R.drawable.ic_failed),
            modifier = Modifier
                .constrainAs(image) {
                    centerVerticallyTo(parent)
                    end.linkTo(text.start)
                }
                .padding(horizontal = 10.dp),
            contentDescription = "icon success or failed"
        )
        Text(
            text = stringResource(textId),
            style = Lato.invoiceIconTitle,
            modifier = Modifier.constrainAs(text) { centerTo(parent) }
        )
    }
}




