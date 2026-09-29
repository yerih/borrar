package com.mivuelto.core.data.datasource

import com.mivuelto.core.data.network.TransactionApiService
import com.mivuelto.core.data.network.asTransactionException
import com.mivuelto.core.data.network.requests.SendChangeRequest
import com.mivuelto.core.data.network.requests.TransactionQueryRequest
import com.mivuelto.core.data.network.responses.TransactionResponse
import com.mivuelto.core.data.network.responses.TransactionStatsResponse
import retrofit2.HttpException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * DataSource delgado sobre [TransactionApiService] (API.md § 2).
 * Traduce cualquier fallo a `TransactionException` (o `AuthException` en 401).
 */
class TransactionRemoteDataSource @Inject constructor(
    private val transactionApi: TransactionApiService
) {
    suspend fun queryTransaction(
        authorization: String,
        request: TransactionQueryRequest
    ): TransactionResponse = try {
        transactionApi.queryTransaction(authorization, request)
    } catch (t: Throwable) {
        throw t.asTransactionException()
    }

    /** 501 esperado hoy; [com.mivuelto.core.data.network.TransactionException.NotImplemented] lo refleja. */
    suspend fun sendChange(
        authorization: String,
        request: SendChangeRequest
    ) {
        try {
            val response = transactionApi.sendChange(authorization, request)
            if (!response.isSuccessful) {
                throw HttpException(response).asTransactionException()
            }
        } catch (t: Throwable) {
            throw t.asTransactionException()
        }
    }

    suspend fun getHistory(authorization: String): List<TransactionResponse> = try {
        transactionApi.getHistory(authorization)
    } catch (t: Throwable) {
        throw t.asTransactionException()
    }

    suspend fun getStats(
        authorization: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): TransactionStatsResponse = try {
        transactionApi.getStats(
            authorization = authorization,
            startDate = startDate.format(API_DATE),
            endDate = endDate.format(API_DATE)
        )
    } catch (t: Throwable) {
        throw t.asTransactionException()
    }

    private companion object {
        val API_DATE: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    }
}
