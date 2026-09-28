package com.mivuelto.core.data.datasource

import com.mivuelto.core.data.network.AuthApiService
import com.mivuelto.core.data.network.asAuthException
import com.mivuelto.core.data.network.requests.LoginRequest
import com.mivuelto.core.data.network.responses.LoginResponse
import retrofit2.HttpException
import javax.inject.Inject

/**
 * DataSource delgado sobre [AuthApiService] (API.md § 1).
 * Traduce cualquier fallo a AuthException; la sesión en memoria
 * la gestiona el repositorio vía SessionManager.
 */
class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApiService
) {
    suspend fun login(request: LoginRequest): LoginResponse = try {
        authApi.login(request)
    } catch (t: Throwable) {
        throw t.asAuthException()
    }

    /** POST /auth/app/logout — 204 sin body. Lanza AuthException si falla. */
    suspend fun logout(sessionToken: String) {
        try {
            val response = authApi.logout("Bearer $sessionToken")
            if (!response.isSuccessful) {
                throw HttpException(response).asAuthException()
            }
        } catch (t: Throwable) {
            throw t.asAuthException()
        }
    }
}
