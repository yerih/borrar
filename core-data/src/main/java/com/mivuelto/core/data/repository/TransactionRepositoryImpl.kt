package com.mivuelto.core.data.repository

import com.mivuelto.core.data.datasource.TransactionRemoteDataSource
import com.mivuelto.core.data.network.requests.SendChangeRequest
import com.mivuelto.core.data.network.requests.TransactionQueryRequest
import com.mivuelto.core.data.network.responses.TransactionResponse
import com.mivuelto.core.data.session.SessionManager
import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.model.DailyStat
import com.mivuelto.core.domain.model.SendChangeCommand
import com.mivuelto.core.domain.model.Transaction
import com.mivuelto.core.domain.model.TransactionQuery
import com.mivuelto.core.domain.model.TransactionStats
import com.mivuelto.core.domain.repository.TransactionRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val remote: TransactionRemoteDataSource,
    private val session: SessionManager
) : TransactionRepository {

    override suspend fun queryTransaction(query: TransactionQuery): Result<Transaction> {
        val authorization = authorization().getOrElse { return Result.failure(it) }
        return remote.queryTransaction(
            authorization = authorization,
            request = TransactionQueryRequest(
                transactionType = query.transactionType.wireValue,
                amount = query.amount,
                date = "2026-08-07",//query.date.format(API_DATE),
                reference = query.reference,
                phone = query.phone,
                document = query.document,
                bankId = query.bankId
            )
        ).map { it.toDomain() }
    }

    override suspend fun sendChange(command: SendChangeCommand): Result<Unit> {
        val authorization = authorization().getOrElse { return Result.failure(it) }
        return remote.sendChange(
            authorization = authorization,
            request = SendChangeRequest(
                amount = command.amount,
                phone = command.phone,
                bankId = command.bankId,
                document = command.document,
                sourceBankId = command.sourceBankId
            )
        )
    }

    override suspend fun getHistory(): Result<List<Transaction>> {
        val authorization = authorization().getOrElse { return Result.failure(it) }
        return remote.getHistory(authorization).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getStats(
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<TransactionStats> {
        val authorization = authorization().getOrElse { return Result.failure(it) }
        return remote.getStats(authorization, startDate, endDate).map { response ->
            TransactionStats(
                totalAmountCents = response.totalAmount.toCents(),
                totalCount = response.totalCount,
                chartData = response.chartData.map { day ->
                    DailyStat(
                        date = day.date,
                        amountCents = day.amount.toCents(),
                        count = day.count
                    )
                }
            )
        }
    }

    /**
     * core-service exige `Authorization: Bearer <token de auth-service>` (§ 2).
     * Sin sesión en memoria no hay request posible: fallar rápido en cliente en
     * lugar de dejar que el gateway responda 401.
     */
    private fun authorization(): Result<String> =
        session.currentToken()?.let { Result.success("Bearer $it") }
            ?: Result.failure(ApiError.Unauthorized("No active session. Login required."))

    private fun TransactionResponse.toDomain() = Transaction(
        id = id,
        amountCents = amount.toCents(),
        transactionTypeId = transactionType,
        statusId = status,
        referenceNumber = referenceNumber,
        cashRegisterId = cashRegisterId,
        toPhone = toPhone,
        toIdDocument = toIdDocument,
        toBankId = toBankId,
        toDocumentType = toDocumentType,
        fromPhone = fromPhone,
        fromIdDocument = fromIdDocument,
        fromBankId = fromBankId,
        fromDocumentType = fromDocumentType,
        response = response,
        isValidated = isValidated,
        created = created,
        updated = updated,
        sessionId = sessionId,
        idempotencyKey = idempotencyKey,
        commerceId = commerceId
    )

    private companion object {
        val API_DATE: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    }
}

/** Convierte el decimal del wire (`150.50`) a centavos, sin coma flotante intermedia. */
internal fun BigDecimal.toCents(): Long =
    movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact()
