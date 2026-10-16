package com.mivuelto.core.data.repository

import com.mivuelto.core.data.datasource.AuthRemoteDataSource
import com.mivuelto.core.data.datasource.UserLocalDataSource
import com.mivuelto.core.data.local.UserEntity
import com.mivuelto.core.data.network.requests.LoginRequest
import com.mivuelto.core.data.network.requests.UserData
import com.mivuelto.core.data.network.responses.LoginResponse
import com.mivuelto.core.data.session.SessionManager
import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.model.AuthSession
import com.mivuelto.core.domain.model.AuthUser
import com.mivuelto.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val session: SessionManager,
    private val local: UserLocalDataSource
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String,
        terminalSerial: String
    ): Result<AuthSession> =
        remote.login(LoginRequest(username, password, terminalSerial)).map { response ->
            session.saveToken(response.sessionToken)
            local.upsert(response.user.toEntity())
            response.toDomain()
        }

    override suspend fun logout(): Result<Unit> {
        val token = session.currentToken()
        val result = if (token == null) {
            Result.success(Unit)
        } else {
            remote.logout(token)
        }
        session.clear()
        local.clear()
        return result
    }

    override fun observeUser(): Flow<AuthUser?> = local.observeUser().map { it?.toDomain() }

    override fun currentToken(): String? = session.currentToken()

    override suspend fun getLocalUser(): Result<AuthUser> =
        local.get()?.let { Result.success(it.toDomain()) }
            ?: Result.failure(ApiError.NotFound("No user stored"))

    private fun LoginResponse.toDomain() = AuthSession(
        sessionToken = sessionToken,
        expiresAt = expiresAt,
        user = user.toDomain()
    )

    private fun UserData.toDomain() = AuthUser(
        id = id,
        username = username,
        roleId = roleId,
        merchantId = merchantId,
        merchantName = merchantName,
        merchantRif = merchantRif
    )

    private fun UserData.toEntity() = UserEntity(
        id = id,
        username = username,
        roleId = roleId,
        merchantId = merchantId,
        merchantName = merchantName,
        merchantRif = merchantRif
    )

    private fun UserEntity.toDomain() = AuthUser(
        id = id,
        username = username,
        roleId = roleId,
        merchantId = merchantId,
        merchantName = merchantName,
        merchantRif = merchantRif
    )
}
