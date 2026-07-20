package com.mivuelto.feature.purchase.ui.invoices

import com.mivuelto.core.domain.model.BankModel


data class InvoiceModel(

    val businessName: String = "comercio",
    val businessBank: String = "banco del comercio",
    val businessBankRif: String = "J-12345678",

    val bank: BankModel = BankModel(),
    val address: String = "localidad o dirección",
    var rif: String = "JXXXXXX",
    var date: String = "0901",
    var time: String = "095959",
    var ref: String = "XXXX",
    var phone: String = "XXXX",
    var amount: String = "999999999,99",

    val hasPrinter: Boolean = false,
)




