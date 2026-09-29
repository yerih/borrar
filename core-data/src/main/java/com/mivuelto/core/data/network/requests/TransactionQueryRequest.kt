package com.mivuelto.core.data.network.requests

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * POST /transactions/query — request (API.md § 2).
 *
 * Reglas del contrato:
 * - `transactionType`: obligatorio, `PAGO_MOVIL` o `TRANSFERENCIA`.
 * - `reference`: opcional; si viene debe cumplir `\d{6,}`.
 * - `amount`: obligatorio y distinto de 0.
 * - `date`: obligatorio, `YYYY-MM-DD`.
 * - `phone`: opcional, **solo** aplica a `PAGO_MOVIL`.
 * - `document`: opcional en `PAGO_MOVIL`, **obligatorio** en `TRANSFERENCIA`.
 * - `bankId`: obligatorio solo si el comercio tiene más de una cuenta activa.
 */
data class TransactionQueryRequest(
    @SerializedName("transactionType")
    val transactionType: String,
    @SerializedName("amount")
    val amount: BigDecimal,
    @SerializedName("date")
    val date: String,
    @SerializedName("reference")
    val reference: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("document")
    val document: String? = null,
    @SerializedName("bankId")
    val bankId: String? = null
)
