package com.mivuelto.core.data.datasource

import com.mivuelto.core.data.network.ApiService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchData() = apiService.getData()
}
