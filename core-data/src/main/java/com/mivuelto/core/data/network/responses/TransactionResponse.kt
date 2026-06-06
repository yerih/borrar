package com.mivuelto.core.data.network.responses


data class TransactionResponse(
    val id: String,
    val cashRegisterId: String? = null,
    val toPhone: String? = null,
    val toIdDocument: String? = null,
    val toBankId: String? = null,
    val toDocumentType: String? = null,
    val fromPhone: String? = null,
    val fromIdDocument: String? = null,
    val fromBankId: String? = null,
    val fromDocumentType: String? = null,
    val transactionType: String,
    val amount: Double,
    val status: String,
    val response: String? = null,
    val referenceNumber: String,
    val isValidated: Boolean? = null,
    val created: String,
    val updated: String? = null,
    val sessionId: String? = null,
    val idempotencyKey: String? = null,
    val commerceId: String? = null
)

