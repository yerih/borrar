package com.mivuelto.core.domain.model

import java.math.BigDecimal
import java.time.LocalDate

/**
 * Verificación de dinero entrante — `POST /transactions/query` (API.md § 2).
 *
 * @property reference opcional; si viene debe cumplir `\d{6,}`.
 * @property phone solo aplica a [TransactionType.PAGO_MOVIL].
 * @property document obligatorio en [TransactionType.TRANSFERENCIA].
 * @property bankId requerido solo si el comercio tiene más de una cuenta activa.
 */
data class TransactionQuery(
    val transactionType: TransactionType,
    val amount: BigDecimal,
    val date: LocalDate,
    val reference: String? = null,
    val phone: String? = null,
    val document: String? = null,
    val bankId: String? = null
)
