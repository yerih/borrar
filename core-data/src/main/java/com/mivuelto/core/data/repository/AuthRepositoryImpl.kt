package com.mivuelto.core.data.repository

import com.mivuelto.core.data.datasource.AuthRemoteDataSource
import com.mivuelto.core.data.network.requests.LoginRequest
import com.mivuelto.core.data.network.responses.LoginResponse
import com.mivuelto.core.data.session.SessionManager
import com.mivuelto.core.domain.model.AuthSession
import com.mivuelto.core.domain.model.AuthUser
import com.mivuelto.core.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val session: SessionManager
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String,
        terminalSerial: String
    ): Result<AuthSession> =
        remote.login(LoginRequest(username, password, terminalSerial)).map { response ->
            session.saveToken(response.sessionToken)
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
        return result
    }

    override fun currentToken(): String? = session.currentToken()

    private fun LoginResponse.toDomain() = AuthSession(
        sessionToken = sessionToken,
        expiresAt = expiresAt,
        user = AuthUser(
            id = user.id,
            username = user.username,
            roleId = user.roleId,
            merchantId = user.merchantId
        )
    )
}
