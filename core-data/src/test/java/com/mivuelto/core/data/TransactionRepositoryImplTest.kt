package com.mivuelto.core.data

import com.mivuelto.core.data.datasource.TransactionRemoteDataSource
import com.mivuelto.core.data.network.requests.DebinTokenRequest
import com.mivuelto.core.data.network.responses.DebinTokenResponse
import com.mivuelto.core.data.repository.TransactionRepositoryImpl
import com.mivuelto.core.data.session.SessionManager
import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.model.AuthSession
import com.mivuelto.core.domain.model.AuthUser
import com.mivuelto.core.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionRepositoryImplTest {

    private val remote: TransactionRemoteDataSource = mockk()
    private val session: SessionManager = mockk()
    private val authRepository: AuthRepository = mockk()
    private val repository = TransactionRepositoryImpl(remote, session, authRepository)

    private fun freshSession(token: String) = AuthSession(
        sessionToken = token,
        expiresAt = "2030-01-01T00:00:00",
        user = AuthUser(id = "u1", username = "admin", roleId = "ADM", merchantId = "m1")
    )

    @Test
    fun `authorization uses the current token while it is still valid`() = runTest {
        every { session.currentToken() } returns "active-token"
        every { session.isTokenExpired() } returns false
        coEvery { remote.getHistory(any()) } returns Result.success(emptyList())

        val result = repository.getHistory()

        assertTrue(result.isSuccess)
        coVerify { remote.getHistory("Bearer active-token") }
        coVerify(exactly = 0) { authRepository.refreshSession() }
    }

    @Test
    fun `authorization refreshes an expired session token before the request`() = runTest {
        every { session.currentToken() } returns "expired-token"
        every { session.isTokenExpired() } returns true
        coEvery { authRepository.refreshSession() } returns Result.success(freshSession("fresh-token"))
        coEvery { remote.getHistory(any()) } returns Result.success(emptyList())

        val result = repository.getHistory()

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { authRepository.refreshSession() }
        coVerify { remote.getHistory("Bearer fresh-token") }
    }

    @Test
    fun `authorization refreshes when there is no session token`() = runTest {
        every { session.currentToken() } returns null
        coEvery { authRepository.refreshSession() } returns Result.success(freshSession("fresh-token"))
        coEvery { remote.debinToken(any(), any()) } returns Result.success(
            DebinTokenResponse(id = "token-id", mensaje = "ok", expiresInSeconds = 120)
        )

        val result = repository.debinToken(
            com.mivuelto.core.domain.model.DebinTokenCommand(
                amount = java.math.BigDecimal("10.00"),
                phone = "04121234567",
                bankId = "bank-1",
                document = "12345678"
            )
        )

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { authRepository.refreshSession() }
        coVerify {
            remote.debinToken(
                authorization = "Bearer fresh-token",
                request = any<DebinTokenRequest>()
            )
        }
    }

    @Test
    fun `authorization fails without calling the API when the refresh fails`() = runTest {
        every { session.currentToken() } returns "expired-token"
        every { session.isTokenExpired() } returns true
        coEvery { authRepository.refreshSession() } returns
            Result.failure(ApiError.Unauthorized("No cached credentials. Login required."))

        val result = repository.getHistory()

        assertTrue(result.exceptionOrNull() is ApiError.Unauthorized)
        coVerify(exactly = 0) { remote.getHistory(any()) }
    }
}
