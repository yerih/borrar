package com.mivuelto.core.domain.model

import java.math.BigDecimal

/**
 * Solicitud de token DEBIN — `POST /transactions/debin/token` (API.md § 2).
 *
 * Paso 1 del débito inmediato; el `id` del resultado se usa en el paso
 * siguiente (`POST /transactions/debin/charge`).
 *
 * @property amount debe ser mayor que 0.
 * @property bankId banco **destino**; debe ser reconocido.
 * @property sourceBankId cuenta origen del comercio; sin integración API
 * activa el backend responde 422.
 */
data class DebinTokenCommand(
    val amount: BigDecimal,
    val phone: String,
    val bankId: String,
    val document: String,
    val sourceBankId: String? = null
)