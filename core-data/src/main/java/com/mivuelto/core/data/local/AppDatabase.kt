package com.mivuelto.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BankEntity::class, UserEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bankDao(): BankDao

    abstract fun userDao(): UserDao
}
