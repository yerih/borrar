package com.mivuelto.core.data.network

import com.mivuelto.core.data.network.requests.LoginRequest
import com.mivuelto.core.data.network.responses.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * auth-service — Login POS / App móvil.
 *
 * Spec: documentation/"API corpocredit"/API.md § 1.
 * Base vía api-gateway: `/auth` (sin cambios de prefijo). Solo roles ADM/CSH.
 */
interface AuthApiService {

    /**
     * POST /auth/app/login — público.
     * 200 -> [LoginResponse]; errores 400 / 401 / 403 / 423 (ver [com.mivuelto.core.domain.error.ApiError]).
     */
    @POST("auth/app/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    /**
     * POST /auth/app/logout — requiere `Authorization: Bearer <sessionToken>`.
     * 204 No Content en éxito, sin body.
     */
    @POST("auth/app/logout")
    suspend fun logout(@Header("Authorization") bearerToken: String): Response<Unit>
}
