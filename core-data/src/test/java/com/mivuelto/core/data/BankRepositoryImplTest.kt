package com.mivuelto.core.data

import com.mivuelto.core.data.datasource.BankLocalDataSource
import com.mivuelto.core.data.datasource.BankRemoteDataSource
import com.mivuelto.core.data.local.BankEntity
import com.mivuelto.core.data.network.responses.BankResponse
import com.mivuelto.core.data.repository.BankRepositoryImpl
import com.mivuelto.core.data.session.SessionManager
import com.mivuelto.core.domain.error.ApiError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BankRepositoryImplTest {

    private val remote: BankRemoteDataSource = mockk()
    private val local: BankLocalDataSource = mockk()
    private val session: SessionManager = mockk()
    private val repository = BankRepositoryImpl(remote, local, session)

    @Before
    fun setup() {
        every { session.currentToken() } returns "session-token"
    }

    @Test
    fun `observeBanks maps stored entities to domain models`() = runTest {
        coEvery { local.observeBanks() } returns flowOf(
            listOf(
                BankEntity(id = "uuid-1", code = "0138", name = "BANCO PLAZA", apiActive = true),
                BankEntity(id = "uuid-2", code = "0105", name = "Mercantil", apiActive = false)
            )
        )

        val banks = repository.observeBanks().first()

        assertEquals(2, banks.size)
        val first = banks.first()
        assertEquals("uuid-1", first.uuid)
        assertEquals("0138", first.code)
        assertEquals("BANCO PLAZA", first.name)
        assertEquals(true, first.apiActive)
        assertEquals(null, first.logo)
        assertEquals(false, banks[1].apiActive)
    }

    @Test
    fun `syncBanks replaces the local catalog and returns the fresh list`() = runTest {
        coEvery { remote.getBanks(any()) } returns Result.success(
            listOf(BankResponse(id = "uuid-1", code = "0138", name = "BANCO PLAZA", apiActive = true))
        )
        coEvery { local.replaceAll(any()) } returns Unit

        val banks = repository.syncBanks().getOrThrow()

        assertEquals(1, banks.size)
        val bank = banks.first()
        assertEquals("uuid-1", bank.uuid)
        assertEquals("0138", bank.code)
        assertEquals("BANCO PLAZA", bank.name)
        assertEquals(true, bank.apiActive)
        coVerify {
            local.replaceAll(
                listOf(BankEntity(id = "uuid-1", code = "0138", name = "BANCO PLAZA", apiActive = true))
            )
        }
    }

    @Test
    fun `syncBanks sends the session bearer token to the remote service`() = runTest {
        coEvery { remote.getBanks(any()) } returns Result.success(emptyList())
        coEvery { local.replaceAll(any()) } returns Unit

        repository.syncBanks()

        coVerify { remote.getBanks("Bearer session-token") }
    }

    @Test
    fun `syncBanks fails fast without a session token`() = runTest {
        every { session.currentToken() } returns null

        val result = repository.syncBanks()

        assertTrue(result.exceptionOrNull() is ApiError.Unauthorized)
        coVerify(exactly = 0) { remote.getBanks(any()) }
        coVerify(exactly = 0) { local.replaceAll(any()) }
    }

    @Test
    fun `syncBanks propagates remote failure without touching local storage`() = runTest {
        val error = Exception("offline")
        coEvery { remote.getBanks(any()) } returns Result.failure(error)

        val result = repository.syncBanks()

        assertEquals(error, result.exceptionOrNull())
        coVerify(exactly = 0) { local.replaceAll(any()) }
    }
}
