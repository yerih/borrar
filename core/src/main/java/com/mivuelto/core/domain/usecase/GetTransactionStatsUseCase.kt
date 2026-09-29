package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.TransactionStats
import com.mivuelto.core.domain.repository.TransactionRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * Métricas de vueltos — `GET /transactions/stats?startDate=&endDate=` (API.md § 2).
 *
 * El backend rechaza con 400 si falta un parámetro, si `endDate < startDate` o
 * si el rango excede 31 días; se valida aquí para dar feedback inmediato.
 */
class GetTransactionStatsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(
        startDate: LocalDate,
        endDate: LocalDate
    ): TransactionStats {
        require(!endDate.isBefore(startDate)) { "endDate must not be before startDate" }
        val days = ChronoUnit.DAYS.between(startDate, endDate) + 1
        require(days <= MAX_RANGE_DAYS) { "date range must not exceed $MAX_RANGE_DAYS days" }
        return repository.getStats(startDate, endDate)
    }

    companion object {
        const val MAX_RANGE_DAYS = 31L
    }
}
