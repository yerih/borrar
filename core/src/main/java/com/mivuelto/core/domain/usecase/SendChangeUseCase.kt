package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.SendChangeCommand
import com.mivuelto.core.domain.repository.TransactionRepository
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Envío de vuelto — `POST /transactions/send-change` (API.md § 2).
 *
 * Valida `amount > 0` y los campos requeridos. El backend responde **501**
 * después de estas validaciones (función no implementada todavía), que el
 * repositorio traduce a `TransactionException.NotImplemented` — no es un
 * error transitorio, así que la UI no debe ofrecer reintento.
 */
class SendChangeUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(command: SendChangeCommand) {
        require(command.amount > BigDecimal.ZERO) { "amount must be greater than 0" }
        require(command.phone.isNotBlank()) { "phone must not be blank" }
        require(command.bankId.isNotBlank()) { "bankId must not be blank" }
        require(command.document.isNotBlank()) { "document must not be blank" }
        repository.sendChange(command)
    }
}
