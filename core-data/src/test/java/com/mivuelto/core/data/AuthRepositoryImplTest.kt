package com.mivuelto.core.data

import com.mivuelto.core.data.datasource.AuthRemoteDataSource
import com.mivuelto.core.data.datasource.UserLocalDataSource
import com.mivuelto.core.data.local.UserEntity
import com.mivuelto.core.data.network.requests.LoginRequest
import com.mivuelto.core.data.network.requests.UserData
import com.mivuelto.core.data.network.responses.LoginResponse
import com.mivuelto.core.data.repository.AuthRepositoryImpl
import com.mivuelto.core.data.session.SessionCredentials
import com.mivuelto.core.data.session.SessionManager
import com.mivuelto.core.domain.error.ApiError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    private val remote: AuthRemoteDataSource = mockk()
    private val session: SessionManager = mockk(relaxed = true)
    private val credentials = SessionCredentials()
    private val local: UserLocalDataSource = mockk()
    private val repository = AuthRepositoryImpl(remote, session, credentials, local)

    private val userEntity = UserEntity(
        id = "user-1",
        username = "V13824919",
        roleId = "role-1",
        merchantId = "merchant-1",
        merchantName = "Dayerling Aular",
        merchantRif = "V13824919"
    )

    private val loginResponse = LoginResponse(
        sessionToken = "token",
        expiresAt = "2026-10-16T07:01:39",
        user = UserData(
            id = "user-1",
            username = "V13824919",
            roleId = "role-1",
            merchantId = "merchant-1",
            merchantName = "Dayerling Aular",
            merchantRif = "V13824919"
        )
    )

    @Before
    fun setup() {
        coEvery { local.upsert(any()) } returns Unit
        coEvery { local.clear() } returns Unit
        every { session.currentToken() } returns "token"
    }

    @Test
    fun `login saves the token and persists the user in room`() = runTest {
        coEvery { remote.login(any()) } returns Result.success(loginResponse)

        val result = repository.login("user", "pass", "POS-0001").getOrThrow()

        verify { session.saveToken("token") }
        coVerify { local.upsert(userEntity) }
        assertEquals("Dayerling Aular", result.user.merchantName)
        assertEquals("V13824919", result.user.merchantRif)
    }

    @Test
    fun `login failure does not touch the stored user`() = runTest {
        coEvery { remote.login(any()) } returns Result.failure(Exception("invalid credentials"))

        val result = repository.login("user", "pass", "POS-0001")

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { local.upsert(any()) }
    }

    @Test
    fun `logout clears the session and the stored user`() = runTest {
        coEvery { remote.logout(any()) } returns Result.success(Unit)

        repository.logout()

        verify { session.clear() }
        coVerify { local.clear() }
    }

    @Test
    fun `refreshSession re-logins with the cached credentials`() = runTest {
        coEvery { remote.login(any()) } returns Result.success(loginResponse)

        repository.login("user", "pass", "POS-0001").getOrThrow()
        val result = repository.refreshSession().getOrThrow()

        coVerify(exactly = 2) { remote.login(LoginRequest("user", "pass", "POS-0001")) }
        assertEquals("token", result.sessionToken)
    }

    @Test
    fun `refreshSession fails when no credentials are cached`() = runTest {
        val result = repository.refreshSession()

        assertTrue(result.exceptionOrNull() is ApiError.Unauthorized)
        coVerify(exactly = 0) { remote.login(any()) }
    }

    @Test
    fun `logout clears the cached credentials`() = runTest {
        coEvery { remote.login(any()) } returns Result.success(loginResponse)
        coEvery { remote.logout(any()) } returns Result.success(Unit)

        repository.login("user", "pass", "POS-0001").getOrThrow()
        repository.logout()

        assertTrue(repository.refreshSession().exceptionOrNull() is ApiError.Unauthorized)
    }

    @Test
    fun `getLocalUser returns the stored user`() = runTest {
        coEvery { local.get() } returns userEntity

        val user = repository.getLocalUser().getOrThrow()

        assertEquals("user-1", user.id)
        assertEquals("Dayerling Aular", user.merchantName)
        assertEquals("V13824919", user.merchantRif)
    }

    @Test
    fun `getLocalUser fails when no user is stored`() = runTest {
        coEvery { local.get() } returns null

        assertTrue(repository.getLocalUser().exceptionOrNull() is ApiError.NotFound)
    }

    @Test
    fun `observeUser maps the stored entity to the domain model`() = runTest {
        coEvery { local.observeUser() } returns flowOf(userEntity)

        val user = repository.observeUser().first()

        assertEquals("user-1", user?.id)
        assertEquals("V13824919", user?.username)
        assertEquals("merchant-1", user?.merchantId)
        assertEquals("Dayerling Aular", user?.merchantName)
        assertEquals("V13824919", user?.merchantRif)
    }

    @Test
    fun `observeUser emits null when no user is stored`() = runTest {
        coEvery { local.observeUser() } returns flowOf(null)

        assertNull(repository.observeUser().first())
    }
}
