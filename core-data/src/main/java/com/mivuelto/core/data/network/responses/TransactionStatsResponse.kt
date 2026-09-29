package com.mivuelto.core.data.network.responses

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/** Un día del agregado de `GET /transactions/stats` (API.md § 2). */
data class DailyStatResponse(
    @SerializedName("date")
    val date: String,
    @SerializedName("amount")
    val amount: BigDecimal,
    @SerializedName("count")
    val count: Int
)

/**
 * GET /transactions/stats?startDate=&endDate= (API.md § 2).
 * Agrega monto y cantidad de **vueltos** por día. Rango máximo 31 días.
 * Uso previsto: dashboard del backoffice. Montos en [BigDecimal] por ser dinero.
 */
data class TransactionStatsResponse(
    @SerializedName("totalAmount")
    val totalAmount: BigDecimal,
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("chartData")
    val chartData: List<DailyStatResponse>
)
