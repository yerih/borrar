package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.Transaction
import com.mivuelto.core.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Historial propio — `GET /transactions/history` (API.md § 2).
 * Últimos 20 **vueltos** del comercio del token; sin paginación ni filtros.
 */
class GetTransactionHistoryUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): Result<List<Transaction>> = repository.getHistory()
}
