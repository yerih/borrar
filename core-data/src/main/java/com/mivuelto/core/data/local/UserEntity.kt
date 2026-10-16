package com.mivuelto.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val roleId: String,
    val merchantId: String,
    val merchantName: String?,
    val merchantRif: String?
)
