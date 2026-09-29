package com.mivuelto.core.domain.model

/**
 * Transacción devuelta por core-service (API.md § 2). Misma forma para Pago Móvil
 * y Transferencia, y para los ítems de `/transactions/history`.
 *
 * Los montos se guardan en **centavos** (`amountCents`), siguiendo la convención
 * del proyecto; el wire los manda como decimal (`150.50`).
 *
 * @property transactionTypeId id de `configs` (tipo `TRANSACCIONES`), no literal.
 * @property statusId id de `configs` (tipo `TRANSACCION_STATUS`), no literal.
 */
data class Transaction(
    val id: String,
    val amountCents: Long,
    val transactionTypeId: String,
    val statusId: String,
    val referenceNumber: String? = null,
    val cashRegisterId: String? = null,
    val toPhone: String? = null,
    val toIdDocument: String? = null,
    val toBankId: String? = null,
    val toDocumentType: String? = null,
    val fromPhone: String? = null,
    val fromIdDocument: String? = null,
    val fromBankId: String? = null,
    val fromDocumentType: String? = null,
    val response: String? = null,
    val isValidated: Boolean? = null,
    val created: String,
    val updated: String? = null,
    val sessionId: String? = null,
    val idempotencyKey: String? = null,
    val commerceId: String? = null
)
