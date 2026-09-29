package com.mivuelto.core.domain.model

import java.math.BigDecimal

/**
 * Envío de vuelto — `POST /transactions/send-change` (API.md § 2).
 *
 * El backend responde **501 siempre** tras pasar las validaciones, porque
 * banking-router-service aún no tiene adaptador de pagos salientes.
 *
 * @property amount debe ser mayor que 0.
 * @property bankId banco **destino**; debe ser reconocido.
 * @property sourceBankId cuenta origen del comercio; sin integración API
 * activa el backend responde 422.
 */
data class SendChangeCommand(
    val amount: BigDecimal,
    val phone: String,
    val bankId: String,
    val document: String,
    val sourceBankId: String? = null
)
