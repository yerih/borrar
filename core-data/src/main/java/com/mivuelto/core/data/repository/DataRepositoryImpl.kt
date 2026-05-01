package com.mivuelto.core.data.repository

import com.mivuelto.core.data.datasource.RemoteDataSource
import com.mivuelto.core.domain.repository.DataRepository
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : DataRepository {
    override suspend fun getData(): Any {
        return remoteDataSource.fetchData()
    }
}
