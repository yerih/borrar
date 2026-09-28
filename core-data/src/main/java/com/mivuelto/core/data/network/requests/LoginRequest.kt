package com.mivuelto.core.data.network.requests

import com.google.gson.annotations.SerializedName

/**
 * POST /auth/app/login — request (API.md § 1). Los 3 campos son obligatorios;
 * vacíos -> 400 en backend.
 */
data class LoginRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("terminalSerial")
    val terminalSerial: String
)
