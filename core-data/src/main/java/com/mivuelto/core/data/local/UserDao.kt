package com.mivuelto.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {

    @Query("SELECT * FROM users LIMIT 1")
    abstract fun observeUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsert(user: UserEntity)

    @Query("DELETE FROM users")
    abstract suspend fun clear()

    @Query("SELECT * FROM users LIMIT 1")
    abstract suspend fun get(): UserEntity?
}
