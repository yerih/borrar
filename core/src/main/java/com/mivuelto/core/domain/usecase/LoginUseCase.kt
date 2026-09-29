package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.AuthSession
import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Login POS (API.md § 1: POST /auth/app/login).
 * Valida blancos en cliente para evitar un 400 evitable; el resto del
 * mapeo de errores (401/403/423) viene de `ApiError` dentro de `Result`.
 */
class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        terminalSerial: String
    ): Result<AuthSession> {
        if (username.isBlank()) return Result.failure(ApiError.Validation("username must not be blank"))
        if (password.isBlank()) return Result.failure(ApiError.Validation("password must not be blank"))
        if (terminalSerial.isBlank()) {
            return Result.failure(ApiError.Validation("terminalSerial must not be blank"))
        }
        return repository.login(username.trim(), password, terminalSerial.trim())
    }
}
