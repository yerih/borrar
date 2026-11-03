package com.mivuelto.core.data.network.responses

import com.google.gson.annotations.SerializedName

/**
 * POST /transactions/debin/token — response (API.md § 2).
 *
 * 201 Created. `id` se usa en el paso siguiente (`POST /transactions/debin/charge`);
 * `expiresInSeconds` indica la vida del token (120 en el spec).
 */
data class DebinTokenResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("expiresInSeconds")
    val expiresInSeconds: Int
)