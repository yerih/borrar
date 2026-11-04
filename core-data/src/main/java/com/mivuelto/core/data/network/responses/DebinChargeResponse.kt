package com.mivuelto.core.data.network.responses

import com.google.gson.annotations.SerializedName

/**
 * POST /transactions/debin/charge — response (API.md § 2).
 *
 * 200 OK con body: `estado` de la operación (p. ej. `"APROBADO"`), `mensaje`
 * de confirmación y la `transaction` registrada — misma forma que
 * [TransactionResponse] de `/transactions/query`.
 */
data class DebinChargeResponse(
    @SerializedName("estado")
    val estado: String,
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("transaction")
    val transaction: TransactionResponse
)