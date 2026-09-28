package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.AuthSession
import com.mivuelto.core.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Login POS (API.md § 1: POST /auth/app/login).
 * Valida blancos en cliente para evitar un 400 evitable; el resto del
 * mapeo de errores (401/403/423) viene de AuthException del repo.
 */
class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        terminalSerial: String
    ): AuthSession {
        require(username.isNotBlank()) { "username must not be blank" }
        require(password.isNotBlank()) { "password must not be blank" }
        require(terminalSerial.isNotBlank()) { "terminalSerial must not be blank" }
        return repository.login(username.trim(), password, terminalSerial.trim())
    }
}
