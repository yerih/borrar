package com.mivuelto.core.domain.model

data class InstantDebitModel(
    var amount: String? = null,
    var phone: String? = null,
    var bank: BankModel? = null
)
