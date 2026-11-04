package com.mivuelto.core.domain.model

/**
 * Confirmación de débito inmediato — response de
 * `POST /transactions/debin/charge` (API.md § 2).
 *
 * @property estado estado de la operación (p. ej. `"APROBADO"`).
 * @property mensaje confirmación del backend.
 * @property transaction transacción registrada para el cobro.
 */
data class DebinChargeResult(
    val estado: String,
    val mensaje: String,
    val transaction: Transaction
)