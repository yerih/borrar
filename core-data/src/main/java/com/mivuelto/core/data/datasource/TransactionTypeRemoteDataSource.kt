package com.mivuelto.core.data.datasource

import com.mivuelto.core.data.network.ApiService
import com.mivuelto.core.data.network.toCoreApiError
import com.mivuelto.core.data.network.responses.ConfigResponse
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class TransactionTypeRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getTransactionTypes(authorization: String): Result<List<ConfigResponse>> = try {
        Result.success(apiService.getConfigs(authorization, "TRANSACCIONES"))
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (t: Throwable) {
        Result.failure(t.toCoreApiError())
    }
}