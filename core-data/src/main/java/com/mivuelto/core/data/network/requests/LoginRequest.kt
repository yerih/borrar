package com.mivuelto.core.data.network.requests


data class LoginRequest(
    val username: String,
    val password: String,
    val terminalSerial: String
)
