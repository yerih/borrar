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
    ): AuthSession {
        val response = remote.login(LoginRequest(username, password, terminalSerial))
        session.saveToken(response.sessionToken)
        return response.toDomain()
    }

    override suspend fun logout() {
        val token = session.currentToken()
        try {
            if (token != null) remote.logout(token)
        } finally {
            session.clear()
        }
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
