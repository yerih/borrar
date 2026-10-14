package com.mivuelto.core.data.repository

import com.mivuelto.core.data.datasource.BankLocalDataSource
import com.mivuelto.core.data.datasource.BankRemoteDataSource
import com.mivuelto.core.data.local.BankEntity
import com.mivuelto.core.data.network.responses.BankResponse
import com.mivuelto.core.data.session.SessionManager
import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.domain.repository.BankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BankRepositoryImpl @Inject constructor(
    private val remote: BankRemoteDataSource,
    private val local: BankLocalDataSource,
    private val session: SessionManager
) : BankRepository {

    override fun observeBanks(): Flow<List<BankModel>> =
        local.observeBanks().map { entities -> entities.map { it.toDomain() } }

    override suspend fun syncBanks(): Result<List<BankModel>> {
        val authorization = authorization().getOrElse { return Result.failure(it) }
        return remote.getBanks(authorization).mapCatching { responses ->
            val banks = responses.map { it.toDomain() }
            local.replaceAll(banks.map { it.toEntity() })
            banks
        }
    }

    private fun authorization(): Result<String> =
        session.currentToken()?.let { Result.success("Bearer $it") }
            ?: Result.failure(ApiError.Unauthorized("No active session. Login required."))

    private fun BankResponse.toDomain() = BankModel(
        logo = null,
        code = code,
        name = name,
        uuid = id,
        apiActive = apiActive
    )

    private fun BankEntity.toDomain() = BankModel(
        logo = null,
        code = code,
        name = name,
        uuid = id,
        apiActive = apiActive
    )

    private fun BankModel.toEntity() = BankEntity(
        id = uuid,
        code = code,
        name = name,
        apiActive = apiActive
    )
}
