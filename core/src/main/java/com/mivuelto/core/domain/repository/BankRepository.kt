package com.mivuelto.core.domain.repository

import com.mivuelto.core.domain.model.BankModel
import kotlinx.coroutines.flow.Flow

/**
 * Catálogo de bancos — API.md § 7. Requiere `Authorization: Bearer <token de
 * sesión>`. La fuente de verdad es la base local (Room); [syncBanks] la
 * refresca desde el backend y [observeBanks] reacciona a esos cambios.
 */
interface BankRepository {

    fun observeBanks(): Flow<List<BankModel>>

    suspend fun syncBanks(): Result<List<BankModel>>
}
