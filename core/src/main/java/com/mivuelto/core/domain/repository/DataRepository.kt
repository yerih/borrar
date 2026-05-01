package com.mivuelto.core.domain.repository

interface DataRepository {
    suspend fun getData(): Any
}
