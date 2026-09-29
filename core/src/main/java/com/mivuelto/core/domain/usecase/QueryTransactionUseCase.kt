package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.model.Transaction
import com.mivuelto.core.domain.model.TransactionQuery
import com.mivuelto.core.domain.model.TransactionType
import com.mivuelto.core.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Verificación de dinero entrante — `POST /transactions/query` (API.md § 2).
 *
 * Aplica en cliente las reglas del contrato para no gastar un 400 en el servidor:
 * monto distinto de 0, `reference` con `\d{6,}` cuando viene, `document`
 * obligatorio en [TransactionType.TRANSFERENCIA] y `phone` descartado para
 * transferencias porque el backend solo lo aplica a Pago Móvil.
 */
class QueryTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(query: TransactionQuery): Transaction {
        require(query.amount != ZERO) { "amount must be different from 0" }

        query.reference?.let { reference ->
            require(reference.length >= MIN_REFERENCE_LENGTH && reference.all(Char::isDigit)) {
                "reference must match \\d{6,}"
            }
        }

        if (query.transactionType == TransactionType.TRANSFERENCIA) {
            require(!query.document.isNullOrBlank()) {
                "document is required for TRANSFERENCIA"
            }
        }

        return repository.queryTransaction(
            query.copy(
                phone = if (query.transactionType == TransactionType.PAGO_MOVIL) query.phone else null
            )
        )
    }

    private companion object {
        val ZERO = java.math.BigDecimal.ZERO
        const val MIN_REFERENCE_LENGTH = 6
    }
}
