package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.TransactionTypeModel
import com.mivuelto.core.domain.repository.ConfigRepository
import javax.inject.Inject

class SyncTransactionTypesUseCase @Inject constructor(
    private val repository: ConfigRepository
) {
    suspend operator fun invoke(): Result<List<TransactionTypeModel>> = repository.syncTransactionTypes()
}