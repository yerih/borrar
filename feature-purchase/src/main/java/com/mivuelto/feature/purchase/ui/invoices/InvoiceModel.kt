package com.mivuelto.feature.purchase.ui.invoices


data class InvoiceModel(

    val businessName: String = "comercio",
    val bankRif: String = "RIFXXXX",
    val bank: String = "banesco",
    val cardBrand: String = "VISA DEBIT",
    val address: String = "localidad o dirección",

    var rif: String = "JXXXXXX",
    var affiliate: String = "afiliado",
    var lot: Long = 1234,
    var terminalNum: String = "terminal bancario",
    var cardNumber: String = "123456789_123456789_",
    var aid: String = "A123456789_123456",
    var date: String = "0901",
    var time: String = "095959",
    var aprb: String = "XXXX",
    var ref: String = "XXXX",
    var recoveryRefNum: String = "XXXX",
    var trace: String = "XXXX",
    var amount: String = "999999999,99",
    var isSale: Boolean = true,
    var trxMode: String = "purchase",

    val hasPrinter: Boolean = false,
    val signId: Int? = null,
    val isDebit: Boolean = false,
//    var accountType: AccountTypeEnum = AccountTypeEnum.CREDIT,
//    val error: ErrorPack? = null,//ErrorPack("05"),
//    val txType: TransactionType = TransactionType.PURCHASE,
//    val quotas: MutableList<ExtraQuota> = mutableListOf(),
    val isSignRequired: Boolean = true
)




