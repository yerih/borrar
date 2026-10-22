package com.mivuelto.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TransactionTypeDao {

    @Query("SELECT * FROM transaction_types ORDER BY name ASC")
    abstract fun observeTransactionTypes(): Flow<List<TransactionTypeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(types: List<TransactionTypeEntity>)

    @Query("DELETE FROM transaction_types")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun replaceAll(types: List<TransactionTypeEntity>) {
        deleteAll()
        insertAll(types)
    }
}