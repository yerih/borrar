package com.mivuelto.core.data.datasource

import com.mivuelto.core.data.local.UserDao
import com.mivuelto.core.data.local.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val dao: UserDao
) {
    fun observeUser(): Flow<UserEntity?> = dao.observeUser()

    suspend fun upsert(user: UserEntity) = dao.upsert(user)

    suspend fun clear() = dao.clear()

    suspend fun get(): UserEntity? = dao.get()
}
