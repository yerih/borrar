package com.mivuelto.core.data.network.requests

import com.google.gson.annotations.SerializedName

/**
 * POST /transactions/debin/charge — request (API.md § 2).
 *
 * Paso 2 del débito inmediato (DEBIN): confirma la operación con el `id`
 * devuelto por `/debin/token` y el código de verificación de 8 dígitos.
 *
 * Responde **200 sin body**; los errores usan el body de error estándar.
 */
data class DebinChargeRequest(
    @SerializedName("id")
    val id: String,
    @SerializedName("token")
    val token: String
)