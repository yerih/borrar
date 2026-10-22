package com.mivuelto.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BankEntity::class, UserEntity::class, TransactionTypeEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bankDao(): BankDao

    abstract fun userDao(): UserDao

    abstract fun transactionTypeDao(): TransactionTypeDao
}
