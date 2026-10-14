package com.mivuelto.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BankDao {

    @Query("SELECT * FROM banks ORDER BY name ASC")
    abstract fun observeBanks(): Flow<List<BankEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(banks: List<BankEntity>)

    @Query("DELETE FROM banks")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun replaceAll(banks: List<BankEntity>) {
        deleteAll()
        insertAll(banks)
    }
}
