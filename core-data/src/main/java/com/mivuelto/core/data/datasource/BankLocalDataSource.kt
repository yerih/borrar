package com.mivuelto.core.data.datasource

import com.mivuelto.core.data.local.BankDao
import com.mivuelto.core.data.local.BankEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BankLocalDataSource @Inject constructor(
    private val dao: BankDao
) {
    fun observeBanks(): Flow<List<BankEntity>> = dao.observeBanks()

    suspend fun replaceAll(banks: List<BankEntity>) = dao.replaceAll(banks)
}
