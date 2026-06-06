package com.mivuelto.core.data.network

import com.mivuelto.core.data.network.requests.LoginRequest
import com.mivuelto.core.data.network.requests.TransactionQueryRequest
import com.mivuelto.core.data.network.responses.BankResponse
import com.mivuelto.core.data.network.responses.ConfigResponse
import com.mivuelto.core.data.network.responses.LoginResponse
import com.mivuelto.core.data.network.responses.TransactionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @POST("app/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("banks")
    suspend fun getBanks(@Header("Authorization") token: String): List<BankResponse>

    @GET("configs")
    suspend fun getConfigs(
        @Header("Authorization") token: String,
        @Query("type") type: String
    ): List<ConfigResponse>

    @POST("transactions/query")
    suspend fun queryTransaction(
        @Header("Authorization") token: String,
        @Body request: TransactionQueryRequest
    ): TransactionResponse

    @GET("router/movimientos")
    suspend fun getBankMovements(
        @Header("Authorization") token: String,
        @Query("bankCode") bankCode: String,
        @Query("rif") rif: String,
        @Query("cuenta") cuenta: String,
        @Query("fechaInicio") fechaInicio: String,
        @Query("fechaFin") fechaFin: String
    ): Any

}





