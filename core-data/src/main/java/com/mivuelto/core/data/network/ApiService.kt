package com.mivuelto.core.data.network

import retrofit2.http.GET

interface ApiService {
    @GET("example/endpoint")
    suspend fun getData(): Any // Placeholder
}
