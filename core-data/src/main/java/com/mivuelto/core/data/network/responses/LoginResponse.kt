package com.mivuelto.core.data.network.responses

import com.google.gson.annotations.SerializedName
import com.mivuelto.core.data.network.requests.UserData

/**
 * POST /auth/app/login 200 (API.md § 1).
 * `sessionToken` es el JWT con `sub=userId`, claims `commerceId`/`roleId`
 * — es el token que consume core-service. `expiresAt` es LocalDateTime
 * (`2026-09-25T15:30:00`, sin zona).
 */
data class LoginResponse(
    @SerializedName("sessionToken")
    val sessionToken: String,
    @SerializedName("expiresAt")
    val expiresAt: String,
    @SerializedName("user")
    val user: UserData
)

