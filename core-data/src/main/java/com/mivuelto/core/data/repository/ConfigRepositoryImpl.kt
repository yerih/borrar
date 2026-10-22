package com.mivuelto.core.data.repository

import com.mivuelto.core.data.datasource.TransactionTypeLocalDataSource
import com.mivuelto.core.data.datasource.TransactionTypeRemoteDataSource
import com.mivuelto.core.data.local.TransactionTypeEntity
import com.mivuelto.core.data.network.responses.ConfigResponse
import com.mivuelto.core.data.session.SessionManager
import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.model.TransactionTypeModel
import com.mivuelto.core.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor(
    private val remote: TransactionTypeRemoteDataSource,
    private val local: TransactionTypeLocalDataSource,
    private val session: SessionManager
) : ConfigRepository {

    override fun observeTransactionTypes(): Flow<List<TransactionTypeModel>> =
        local.observeTransactionTypes().map { entities -> entities.map { it.toDomain() } }

    override suspend fun syncTransactionTypes(): Result<List<TransactionTypeModel>> {
        val authorization = authorization().getOrElse { return Result.failure(it) }
        return remote.getTransactionTypes(authorization).mapCatching { responses ->
            val types = responses.map { it.toDomain() }
            local.replaceAll(types.map { it.toEntity() })
            types
        }
    }

    private fun authorization(): Result<String> =
        session.currentToken()?.let { Result.success("Bearer $it") }
            ?: Result.failure(ApiError.Unauthorized("No active session. Login required."))

    private fun ConfigResponse.toDomain() = TransactionTypeModel(
        id = key,
        name = value
    )

    private fun TransactionTypeEntity.toDomain() = TransactionTypeModel(
        id = id,
        name = name
    )

    private fun TransactionTypeModel.toEntity() = TransactionTypeEntity(
        id = id,
        name = name
    )
}