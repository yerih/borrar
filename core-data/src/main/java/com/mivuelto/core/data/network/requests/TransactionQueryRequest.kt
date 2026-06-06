package com.mivuelto.core.data.network.requests


data class TransactionQueryRequest(
    val reference: String,
    val amount: Double,
    val date: String,
    val phone: String,
    val document: String,
    val bankId: String? = null // Opcional, requerido solo si hay múltiples cuentas
)




