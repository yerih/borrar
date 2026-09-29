package com.mivuelto.core.data.network

import com.mivuelto.core.data.network.requests.LoginRequest
import com.mivuelto.core.data.network.responses.BankResponse
import com.mivuelto.core.data.network.responses.ConfigResponse
import com.mivuelto.core.data.network.responses.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Catálogos públicos de backoffice-service (API.md § 7) — rutas sin prefijo
 * `/backoffice` y sin autenticación (`permitAll`), pensadas para poblar selects.
 *
 * Endpoints de auth-service y core-service viven en [AuthApiService] y
 * [TransactionApiService]: un cliente por microservicio, igual que la spec.
 */
interface ApiService {

    /**
     * @deprecated Usar [AuthApiService.login]. Se mantiene solo por compatibilidad;
     * el path canónico es `auth/app/login` (API.md § 1, base `/auth` vía gateway).
     */
    @Deprecated("Usar AuthApiService.login", ReplaceWith("authApiService.login(request)"))
    @POST("auth/app/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    /**
     * GET /banks — bancos activos, ordenados por nombre.
     * Sin `apiActive` devuelve el listado completo; con `apiActive=true` solo los
     * que tienen integración API activa (hoy solo Banco Plaza).
     */
    @GET("banks")
    suspend fun getBanks(
        @Query("apiActive") apiActive: Boolean?
    ): List<BankResponse>

    /**
     * GET /configs?type= — `key` es el id de la fila `configs`, el DTO lo renombra.
     * Tipos usados: `TIPO_DOCUMENTO`, `OPERADORAS`, `TRANSACCION_STATUS`,
     * `TRANSACCIONES`, `MONEDAS`, `BCV`, `BCV_EUR`, `CAJA_STATUS`.
     * 400 si `type` está ausente (parámetro requerido, sin default).
     */
    @GET("configs")
    suspend fun getConfigs(
        @Query("type") type: String
    ): List<ConfigResponse>
}
