package com.mivuelto.feature.purchase.ui.invoices


data class InvoiceModel(

    val businessName: String = "comercio",
    val bankRif: String = "RIFXXXX",
    val bank: String = "banesco",
    val address: String = "localidad o dirección",

    var rif: String = "JXXXXXX",
    var date: String = "0901",
    var time: String = "095959",
    var ref: String = "XXXX",
    var bankOrigin: String = "XXXX",
    var phone: String = "XXXX",
    var amount: String = "999999999,99",

    val hasPrinter: Boolean = false,
)




