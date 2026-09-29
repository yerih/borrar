package com.mivuelto.core.data.di

import com.mivuelto.core.data.repository.AuthRepositoryImpl
import com.mivuelto.core.data.repository.DataRepositoryImpl
import com.mivuelto.core.data.repository.TransactionRepositoryImpl
import com.mivuelto.core.domain.repository.AuthRepository
import com.mivuelto.core.domain.repository.DataRepository
import com.mivuelto.core.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDataRepository(
        dataRepositoryImpl: DataRepositoryImpl
    ): DataRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository
}
