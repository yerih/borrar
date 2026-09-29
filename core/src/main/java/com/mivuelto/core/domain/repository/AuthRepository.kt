package com.mivuelto.core.domain.repository

import com.mivuelto.core.domain.model.AuthSession

/**
 * Contrato de auth-service (API.md § 1 — login POS / app móvil).
 * Implementación en `:core-data` ([com.mivuelto.core.data.repository.AuthRepositoryImpl]).
 * Toda operación devuelve `Result<T>`: los fallos son un [com.mivuelto.core.domain.error.ApiError]
 * (401 credenciales, 403 rol, 423 bloqueado, ...) en `Result.failure`, nunca excepciones lanzadas.
 */
interface AuthRepository {
    /** POST /auth/app/login (público). Guarda la sesión en memoria si tiene éxito. */
    suspend fun login(
        username: String,
        password: String,
        terminalSerial: String
    ): Result<AuthSession>

    /** POST /auth/app/logout con el token guardado. Limpia la sesión local siempre. */
    suspend fun logout(): Result<Unit>

    /** Token JWT actual en memoria, o null si no hay sesión. */
    fun currentToken(): String?

    /** Header listo para Retrofit: `"Bearer <token>"` o null. */
    fun authorizationHeader(): String? = currentToken()?.let { "Bearer $it" }
}
