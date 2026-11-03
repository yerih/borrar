package com.mivuelto.core.domain.model

/**
 * Confirmación de débito inmediato — `POST /transactions/debin/charge` (API.md § 2).
 *
 * Paso 2 DEBIN: usa el `id` recibido en [DebinToken] más el código de
 * verificación de 8 dígitos.
 *
 * @property id obtenido del response de `/debin/token`.
 * @property token código de verificación de 8 dígitos.
 */
data class DebinChargeCommand(
    val id: String,
    val token: String
)