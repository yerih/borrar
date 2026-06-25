package com.mivuelto.core.domain.model

data class CheckPaymentModel(
    var reference: String? = null,
    val amount: String? = null,
    val phone: String? = null,
    val bank: String? = null
)

