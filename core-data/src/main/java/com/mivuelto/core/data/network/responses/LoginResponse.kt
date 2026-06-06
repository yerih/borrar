package com.mivuelto.core.data.network.responses

import com.mivuelto.core.data.network.requests.UserData


data class LoginResponse(
    val sessionToken: String,
    val expiresAt: String,
    val user: UserData
)

