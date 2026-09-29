package com.mivuelto.core.data.network.requests

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * POST /transactions/send-change — request (API.md § 2).
 *
 * `amount` debe ser > 0 (si no, 400). `bankId` es el banco **destino** y debe
 * ser reconocido (si no, 400). `sourceBankId` es la cuenta origen del comercio;
 * si su banco no tiene integración API activa el backend responde 422.
 *
 * El endpoint responde **501 siempre** tras pasar las validaciones: no hay
 * adaptador saliente en banking-router-service todavía.
 */
data class SendChangeRequest(
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
