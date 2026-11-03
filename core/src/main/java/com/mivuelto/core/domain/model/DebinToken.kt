package com.mivuelto.core.domain.model

/**
 * Token de débito inmediato — `POST /transactions/debin/token` (API.md § 2).
 *
 * Resultado del paso 1 DEBIN: el [id] se envía junto al código de verificación
 * en `POST /transactions/debin/charge`.
 *
 * @property mensaje confirmación del backend (p. ej. `"0000 - Transaccion Exitosa"`).
 * @property expiresInSeconds vida útil del token en segundos.
 */
data class DebinToken(
    val id: String,
    val mensaje: String,
    val expiresInSeconds: Int
)