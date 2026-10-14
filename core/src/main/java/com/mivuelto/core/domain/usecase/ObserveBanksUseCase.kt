package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Stream reactivo del catálogo de bancos desde la base local (Room).
 * Emite cada vez que [SyncBanksUseCase] refresca la tabla.
 */
class ObserveBanksUseCase @Inject constructor(
    private val repository: BankRepository
) {
    operator fun invoke(): Flow<List<BankModel>> = repository.observeBanks()
}
