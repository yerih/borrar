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

    fun saveToken(token: String) {
        _token.value = token
    }

    fun clear() {
        _token.value = null
    }

    fun currentToken(): String? = _token.value
}
