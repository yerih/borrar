package com.mivuelto.core.data.network.responses

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Entidad `Transaction` de core-service (API.md § 2). Misma forma para
 * Pago Móvil y Transferencia, y también para los ítems de `/transactions/history`.
 *
 * `transactionType` y `status` son **ids de `configs`**, no literales: hay que
 * resolverlos contra `GET /configs?type=TRANSACCIONES` /
 * `GET /configs?type=TRANSACCION_STATUS` para obtener el valor legible.
 *
 * `amount` es [BigDecimal] y no `Double` a propósito: el dato es dinero y el
 * dominio lo guarda en centavos, así que evita multiplicar y redondear en coma
 * flotante.
 */
data class TransactionResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("amount")
    val amount: BigDecimal,
    @SerializedName("transactionType")
    val transactionType: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("referenceNumber")
    val referenceNumber: String? = null,
    @SerializedName("cashRegisterId")
    val cashRegisterId: String? = null,
    @SerializedName("toPhone")
    val toPhone: String? = null,
    @SerializedName("toIdDocument")
    val toIdDocument: String? = null,
    @SerializedName("toBankId")
    val toBankId: String? = null,
    @SerializedName("toDocumentType")
    val toDocumentType: String? = null,
    @SerializedName("fromPhone")
    val fromPhone: String? = null,
    @SerializedName("fromIdDocument")
    val fromIdDocument: String? = null,
    @SerializedName("fromBankId")
    val fromBankId: String? = null,
    @SerializedName("fromDocumentType")
    val fromDocumentType: String? = null,
    @SerializedName("response")
    val response: String? = null,
    @SerializedName("isValidated")
    val isValidated: Boolean? = null,
    @SerializedName("created")
    val created: String,
    @SerializedName("updated")
    val updated: String? = null,
    @SerializedName("sessionId")
    val sessionId: String? = null,
    @SerializedName("idempotencyKey")
    val idempotencyKey: String? = null,
    @SerializedName("commerceId")
    val commerceId: String? = null
)
