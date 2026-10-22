package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.TransactionTypeModel
import com.mivuelto.core.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTransactionTypesUseCase @Inject constructor(
    private val repository: ConfigRepository
) {
    operator fun invoke(): Flow<List<TransactionTypeModel>> = repository.observeTransactionTypes()
}