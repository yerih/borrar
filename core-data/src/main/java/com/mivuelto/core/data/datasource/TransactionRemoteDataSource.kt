package com.mivuelto.core.data.datasource

import com.mivuelto.core.data.network.TransactionApiService
import com.mivuelto.core.data.network.toCoreApiError
import com.mivuelto.core.data.network.requests.SendChangeRequest
import com.mivuelto.core.data.network.requests.TransactionQueryRequest
import com.mivuelto.core.data.network.responses.TransactionResponse
import com.mivuelto.core.data.network.responses.TransactionStatsResponse
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * DataSource delgado sobre [TransactionApiService] (API.md § 2).
 * Devuelve `Result<T>` en lugar de lanzar: cualquier fallo de red o HTTP se
 * traduce a `ApiError` y viaja dentro de `Result.failure`.
 */
class TransactionRemoteDataSource @Inject constructor(
    private val transactionApi: TransactionApiService
) {
    suspend fun queryTransaction(
        authorization: String,
        request: TransactionQueryRequest
    ): Result<TransactionResponse> = try {
        Result.success(transactionApi.queryTransaction(authorization, request))
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (t: Throwable) {
        Result.failure(t.toCoreApiError())
    }

    /** 501 esperado hoy; [com.mivuelto.core.domain.error.ApiError.NotImplemented] lo refleja. */
    suspend fun sendChange(
        authorization: String,
        request: SendChangeRequest
    ): Result<Unit> {
        val response = try {
            transactionApi.sendChange(authorization, request)
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (t: Throwable) {
            return Result.failure(t.toCoreApiError())
        }

        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(HttpException(response).toCoreApiError())
        }
    }

    suspend fun getHistory(authorization: String): Result<List<TransactionResponse>> = try {
        Result.success(transactionApi.getHistory(authorization))
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (t: Throwable) {
        Result.failure(t.toCoreApiError())
    }

    suspend fun getStats(
        authorization: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<TransactionStatsResponse> = try {
        Result.success(
            transactionApi.getStats(
                authorization = authorization,
                startDate = startDate.format(API_DATE),
                endDate = endDate.format(API_DATE)
            )
        )
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (t: Throwable) {
        Result.failure(t.toCoreApiError())
    }

    private companion object {
        val API_DATE: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    }
}
