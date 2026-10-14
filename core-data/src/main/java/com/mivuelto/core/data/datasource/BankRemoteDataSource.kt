package com.mivuelto.core.data.datasource

import com.mivuelto.core.data.network.BankApiService
import com.mivuelto.core.data.network.toCoreApiError
import com.mivuelto.core.data.network.responses.BankResponse
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

/**
 * DataSource delgado sobre [BankApiService] (API.md § 7). Exige
 * `Authorization: Bearer <token de sesión>`. Devuelve `Result<T>` en lugar de
 * lanzar: cualquier fallo de red o HTTP se traduce a `ApiError` dentro de
 * `Result.failure`.
 */
class BankRemoteDataSource @Inject constructor(
    private val bankApi: BankApiService
) {
    suspend fun getBanks(authorization: String): Result<List<BankResponse>> = try {
        Result.success(bankApi.getBanks(authorization).filter{ it.apiActive })
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (t: Throwable) {
        Result.failure(t.toCoreApiError())
    }
}
