package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.AuthUser
import com.mivuelto.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Stream reactivo del usuario autenticado persistido en la base local
 * (Room). Emite en cada login y se vacía en cada logout.
 */
class ObserveUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<AuthUser?> = repository.observeUser()
}
