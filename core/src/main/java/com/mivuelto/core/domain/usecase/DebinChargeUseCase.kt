package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.model.DebinChargeCommand
import com.mivuelto.core.domain.model.DebinChargeResult
import com.mivuelto.core.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Débito inmediato, paso 2 — `POST /transactions/debin/charge` (API.md § 2).
 *
 * Confirma la operación con el `id` del paso anterior y el código de
 * verificación. El backend responde **200** con `estado`, `mensaje` y la
 * `transaction` registrada; los errores llegan en el body de error estándar.
 */
class DebinChargeUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(command: DebinChargeCommand): Result<DebinChargeResult> {
        if (command.id.isBlank()) {
            return Result.failure(ApiError.Validation("id must not be blank"))
        }
        if (command.token.isBlank()) {
            return Result.failure(ApiError.Validation("token must not be blank"))
        }
        return repository.debinCharge(command)
    }
}