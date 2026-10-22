package com.mivuelto.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_types")
data class TransactionTypeEntity(
    @PrimaryKey val id: String,
    val name: String
)