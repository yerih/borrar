package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.model.DebinToken
import com.mivuelto.core.domain.model.DebinTokenCommand
import com.mivuelto.core.domain.repository.TransactionRepository
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Débito inmediato, paso 1 — `POST /transactions/debin/token` (API.md § 2).
 *
 * Valida `amount > 0` y los campos requeridos, luego reserva el token. El
 * resultado ([DebinToken.id]) se usa en el paso siguiente
 * (`POST /transactions/debin/charge`).
 */
class DebinTokenUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(command: DebinTokenCommand): Result<DebinToken> {
        if (command.amount <= BigDecimal.ZERO) {
            return Result.failure(ApiError.Validation("amount must be greater than 0"))
        }
        if (command.phone.isBlank()) {
            return Result.failure(ApiError.Validation("phone must not be blank"))
        }
        if (command.bankId.isBlank()) {
            return Result.failure(ApiError.Validation("bankId must not be blank"))
        }
        if (command.document.isBlank()) {
            return Result.failure(ApiError.Validation("document must not be blank"))
        }
        return repository.debinToken(command)
    }
}