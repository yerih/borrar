package com.mivuelto.core.data.datasource

import com.mivuelto.core.data.network.AuthApiService
import com.mivuelto.core.data.network.toAuthApiError
import com.mivuelto.core.data.network.requests.LoginRequest
import com.mivuelto.core.data.network.responses.LoginResponse
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import javax.inject.Inject

/**
 * DataSource delgado sobre [AuthApiService] (API.md § 1).
 * Devuelve `Result<T>` en lugar de lanzar: cualquier fallo de red o HTTP se
 * traduce a `ApiError` y viaja dentro de `Result.failure`. La sesión en memoria
 * la gestiona el repositorio vía SessionManager.
 */
class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApiService
) {
    suspend fun login(request: LoginRequest): Result<LoginResponse> = try {
        Result.success(authApi.login(request))
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (t: Throwable) {
        Result.failure(t.toAuthApiError())
    }

    /** POST /auth/app/logout — 204 sin body. `ApiError` si falla. */
    suspend fun logout(sessionToken: String): Result<Unit> {
        val response = try {
            authApi.logout("Bearer $sessionToken")
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (t: Throwable) {
            return Result.failure(t.toAuthApiError())
        }

        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(HttpException(response).toAuthApiError())
        }
    }
}
