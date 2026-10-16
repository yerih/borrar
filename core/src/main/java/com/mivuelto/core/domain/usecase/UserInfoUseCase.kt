package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.AuthUser
import com.mivuelto.core.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * One-shot del usuario autenticado persistido en la base local (Room).
 * Devuelve [com.mivuelto.core.domain.error.ApiError.NotFound] en
 * `Result.failure` si no hay usuario guardado.
 */
class UserInfoUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<AuthUser> = repository.getLocalUser()
}
