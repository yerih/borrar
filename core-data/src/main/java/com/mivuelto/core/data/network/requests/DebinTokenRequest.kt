package com.mivuelto.core.data.network.requests

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * POST /transactions/debin/token — request (API.md § 2).
 *
 * Paso 1 del débito inmediato (DEBIN). `amount` debe ser > 0 (si no, 400),
 * `bankId` es el banco **destino** y debe ser reconocido. `sourceBankId` es la
 * cuenta origen del comercio; si su banco no tiene integración API activa el
 * backend responde 422.
 *
 * Responde **201** con el `id` que se envía en el paso siguiente
 * (`POST /transactions/debin/charge`).
 */
data class DebinTokenRequest(
    @SerializedName("amount")
    val amount: BigDecimal,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("bankId")
    val bankId: String,
    @SerializedName("document")
    val document: String,
    @SerializedName("sourceBankId")
    val sourceBankId: String? = null
)