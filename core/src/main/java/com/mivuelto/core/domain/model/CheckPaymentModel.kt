package com.mivuelto.core.domain.model

data class CheckPaymentModel(
    var reference: String? = null,
    var amount: String? = null,
    var phone: String? = null,
    var bank: String? = null
)

