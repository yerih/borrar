package com.mivuelto.core.data.network

import com.mivuelto.core.data.network.requests.SendChangeRequest
import com.mivuelto.core.data.network.requests.TransactionQueryRequest
import com.mivuelto.core.data.network.responses.TransactionResponse
import com.mivuelto.core.data.network.responses.TransactionStatsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * core-service — Motor transaccional (app móvil).
 *
 * Spec: documentation/"API corpocredit"/API.md § 2. Base `/transactions`.
 * Todos los endpoints exigen `Authorization: Bearer <token de auth-service>`
 * (el filtro JWT lee `sub` como userId y el claim `commerceId`).
 */
interface TransactionApiService {

    /**
     * POST /transactions/query — verifica dinero entrante.
     * 200 -> [TransactionResponse]. `transactionType` del body decide el adaptador
     * consultado (`/router/pago-movil` o `/router/transferencia`).
     */
    @POST("transactions/query")
    suspend fun queryTransaction(
        @Header("Authorization") authorization: String,
        @Body request: TransactionQueryRequest
    ): TransactionResponse

    /**
     * POST /transactions/send-change — envío de vuelto (pago móvil saliente).
     * El backend responde **501 siempre** tras pasar las validaciones (§ 2), por eso
     * se devuelve [Response] y no un body: hoy no hay respuesta tipada.
     */
    @POST("transactions/send-change")
    suspend fun sendChange(
        @Header("Authorization") authorization: String,
        @Body request: SendChangeRequest
    ): Response<Unit>

    /**
     * GET /transactions/history — últimos 20 **vueltos** del comercio del token.
     * Sin paginación ni filtros.
     */
    @GET("transactions/history")
    suspend fun getHistory(
        @Header("Authorization") authorization: String
    ): List<TransactionResponse>

    /**
     * GET /transactions/stats?startDate=&endDate= — agregados para dashboard.
     * Rango máximo 31 días; 400 si falta un parámetro, si `endDate < startDate`
     * o si el rango excede 31 días.
     */
    @GET("transactions/stats")
    suspend fun getStats(
        @Header("Authorization") authorization: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): TransactionStatsResponse
}
