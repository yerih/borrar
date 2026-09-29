package com.mivuelto.core.domain.model

/**
 * Tipo de operación de `POST /transactions/query` (API.md § 2).
 * Decide qué adaptador de banking-router-service consulta el backend:
 * `/router/pago-movil` o `/router/transferencia`.
 */
enum class TransactionType(val wireValue: String) {
    PAGO_MOVIL("PAGO_MOVIL"),
    TRANSFERENCIA("TRANSFERENCIA");

    companion object {
        fun fromWire(value: String): TransactionType? =
            values().firstOrNull { it.wireValue.equals(value, ignoreCase = true) }
    }
}
