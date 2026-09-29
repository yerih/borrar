package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.model.SendChangeCommand
import com.mivuelto.core.domain.repository.TransactionRepository
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Envío de vuelto — `POST /transactions/send-change` (API.md § 2).
 *
 * Valida `amount > 0` y los campos requeridos. El backend responde **501**
 * después de estas validaciones (función no implementada todavía), que el
 * repositorio traduce a `ApiError.NotImplemented` — no es un error transitorio,
 * así que la UI no debe ofrecer reintento.
 */
class SendChangeUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(command: SendChangeCommand): Result<Unit> {
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
        return repository.sendChange(command)
    }
}
