package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.AuthSession
import com.mivuelto.core.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Refresca el token de sesión re-logueando con las credenciales en memoria
 * (API.md § 1 — no existe endpoint de refresh; el único camino es
 * `POST /auth/app/login`).
 *
 * Llamar antes de cada operación transaccional para garantizar un token
 * vigente. Si no hay credenciales cacheadas falla con
 * `ApiError.Unauthorized` (proceso nuevo o logout).
 */
class RefreshSessionUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<AuthSession> = repository.refreshSession()
}