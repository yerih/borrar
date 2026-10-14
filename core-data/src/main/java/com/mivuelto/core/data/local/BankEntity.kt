package com.mivuelto.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banks")
data class BankEntity(
    @PrimaryKey val id: String,
    val code: String,
    val name: String,
    val apiActive: Boolean
)
