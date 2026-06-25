package com.mivuelto.feature.purchase.ui.invoices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mivuelto.core.checkAmount
import com.mivuelto.core.formatDate
import com.mivuelto.core.formatTime
import com.mivuelto.core.ui.BaseScreen
import com.mivuelto.core.ui.design.buttons.ButtonBorder
import com.mivuelto.core.ui.design.buttons.ButtonFilled
import com.mivuelto.core.ui.design.logos.CorpoCreditLogo
import com.mivuelto.core.ui.theme.Lato


@Composable
fun InvoiceScreen(
    data: InvoiceModel = InvoiceModel(),
    onBack: () -> Unit = {},
    onTaskDone: ()->Unit = {}
) {
    BaseScreen(onBack) {
        Scaffold(topBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CorpoCreditLogo(modifier = Modifier.padding(top = 5.dp))
            }
        }) {
            Box{

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(it)
                ) {
                    items(1) {

                        InvoiceIconTitle(
                            textId = if (data.trxMode == "void") com.mivuelto.core.ui.R.string.void_successfull else com.mivuelto.core.ui.R.string.transaction_successful,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .padding(horizontal = 20.dp)
                        ) {
                            var style = Lato.invoiceMerchant
                            with(data) {
                                Spacer(Modifier.height(10.dp))

                                InvoiceElement(center = "$bank $bankRif", style)
                                InvoiceElement(center = "Recibo de compra", style)
                                Spacer(Modifier.height(10.dp))

                                style = Lato.invoiceAddress
                                InvoiceElement(center = businessName, style)
                                InvoiceElement(center = address, style)
                                Spacer(Modifier.height(10.dp))

                                InvoiceElement(label = "rif", value = rif)

                                style = style.copy(fontSize = 16.sp)
                                Spacer(Modifier.height(10.dp))
                                InvoiceElement(center = cardNumber, style)
                                Spacer(Modifier.height(10.dp))

                                InvoiceElement(label = "fecha", value = date.formatDate())
                                InvoiceElement(label = "hora", value = time.formatTime())
                                InvoiceElement(label = "ref", value = ref)

                                Spacer(Modifier.height(15.dp))
                                InvoiceElement(
                                    label = "monto",
                                    value = "Bs. ${amount.checkAmount()}",
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                                        .padding(bottom = 10.dp),
                                    withoutFontWeight = false
                                )

                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp).align(Alignment.BottomCenter)) {

                    if (!data.hasPrinter)
                        ButtonFilled(
                            textId = com.mivuelto.core.ui.R.string.exit,
                            modifier = Modifier.padding(top = 10.dp),
                            onClick = {
                                onTaskDone()
                            }
                        )
                    else
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ButtonBorder(
                                textId = com.mivuelto.core.ui.R.string.exit,
                                modifier = Modifier.weight(1f),
                                paddingHz = 0.dp,
                                onClick = {
                                    onTaskDone()
                                },
                            )

                            ButtonFilled(
                                textId = com.mivuelto.core.ui.R.string.print,
                                modifier = Modifier.weight(1f),
                                paddingHz = 0.dp,
                                onClick = {
                                },
                            )
                        }
                }
            }
        }
    }
}


