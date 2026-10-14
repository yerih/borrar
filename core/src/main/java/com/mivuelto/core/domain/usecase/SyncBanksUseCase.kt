package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.domain.repository.BankRepository
import javax.inject.Inject

/**
 * Refresco del catálogo de bancos — `GET /banks` (API.md § 7), con
 * `Authorization: Bearer <token de sesión>`. Reemplaza el contenido de la
 * base local. Un fallo no afecta la base existente: el stream sigue sirviendo
 * los bancos cacheados.
 */
class SyncBanksUseCase @Inject constructor(
    private val repository: BankRepository
) {
    suspend operator fun invoke(): Result<List<BankModel>> = repository.syncBanks()
}
