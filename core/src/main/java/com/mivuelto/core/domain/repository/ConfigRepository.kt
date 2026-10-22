package com.mivuelto.core.domain.repository

import com.mivuelto.core.domain.model.TransactionTypeModel
import kotlinx.coroutines.flow.Flow

/**
 * Catálogo de tipos de transacción — `GET /configs?type=TRANSACCIONES` (API.md § 7).
 * La fuente de verdad es la base local (Room); [syncTransactionTypes] la
 * refresca desde el backend y [observeTransactionTypes] reacciona a esos cambios.
 */
interface ConfigRepository {

    fun observeTransactionTypes(): Flow<List<TransactionTypeModel>>

    suspend fun syncTransactionTypes(): Result<List<TransactionTypeModel>>
}