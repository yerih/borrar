package com.mivuelto.core.data.datasource

import com.mivuelto.core.data.local.TransactionTypeDao
import com.mivuelto.core.data.local.TransactionTypeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionTypeLocalDataSource @Inject constructor(
    private val dao: TransactionTypeDao
) {
    fun observeTransactionTypes(): Flow<List<TransactionTypeEntity>> = dao.observeTransactionTypes()

    suspend fun replaceAll(types: List<TransactionTypeEntity>) = dao.replaceAll(types)
}