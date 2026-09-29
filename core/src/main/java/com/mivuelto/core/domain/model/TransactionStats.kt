package com.mivuelto.core.domain.model

/**
 * Agregado de vueltos por día — `GET /transactions/stats` (API.md § 2).
 * Uso previsto: dashboard. Rango máximo 31 días.
 *
 * @property totalAmountCents total del rango, en centavos.
 */
data class TransactionStats(
    val totalAmountCents: Long,
    val totalCount: Int,
    val chartData: List<DailyStat>
)

/** Un día del agregado, con monto en centavos. */
data class DailyStat(
    val date: String,
    val amountCents: Long,
    val count: Int
)
