package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Logout POS (API.md § 1: POST /auth/app/logout, 204).
 * Invalida la sesión en backend (`sessions.active = false`); el JWT sigue
 * válido criptográficamente hasta expirar (sin blacklist, según spec).
 */
class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.logout()
}
