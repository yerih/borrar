package com.mivuelto.core.data.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sesión POS en memoria (token JWT de POST /auth/app/login).
 * El JWT no se invalida criptográficamente con logout (API.md § 1),
 * así que basta con limpiar el holder local tras el 204.
 */
@Singleton
class SessionManager @Inject constructor() {
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token.asStateFlow()

    private var issuedAtMillis = 0L

    fun saveToken(token: String) {
        _token.value = token
        issuedAtMillis = System.currentTimeMillis()
    }

    fun clear() {
        _token.value = null
        issuedAtMillis = 0L
    }

    fun currentToken(): String? = _token.value

    /**
     * El token de sesión dura 1 hora: sin token o vencido ese TTL, hay que
     * re-loguear antes de la siguiente llamada transaccional.
     */
    fun isTokenExpired(): Boolean {
        if (_token.value == null) return true
        return System.currentTimeMillis() - issuedAtMillis >= TOKEN_TTL_MILLIS
    }

    private companion object {
        const val TOKEN_TTL_MILLIS = 60 * 60 * 1000L
    }
}
