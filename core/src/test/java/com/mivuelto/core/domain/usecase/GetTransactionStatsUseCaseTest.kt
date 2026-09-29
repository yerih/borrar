package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class GetTransactionStatsUseCaseTest {

    private val repository = mockk<TransactionRepository>(relaxed = true)
    private val useCase = GetTransactionStatsUseCase(repository)

    @Test
    fun `range of exactly 31 days is allowed`() = runTest {
        val start = LocalDate.of(2026, 9, 1)
        val end = LocalDate.of(2026, 10, 1)
        coEvery { repository.getStats(any(), any()) } returns Result.success(mockk())

        useCase(start, end)

        coVerify { repository.getStats(start, end) }
    }

    @Test
    fun `range longer than 31 days is rejected`() = runTest {
        val start = LocalDate.of(2026, 9, 1)
        val end = LocalDate.of(2026, 10, 2)

        val result = useCase(start, end)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApiError.Validation)
    }

    @Test
    fun `endDate before startDate is rejected`() = runTest {
        val result = useCase(LocalDate.of(2026, 9, 25), LocalDate.of(2026, 9, 1))

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApiError.Validation)
    }

    @Test
    fun `single day range is allowed`() = runTest {
        val day = LocalDate.of(2026, 9, 25)
        coEvery { repository.getStats(any(), any()) } returns Result.success(mockk())

        useCase(day, day)

        coVerify { repository.getStats(day, day) }
    }
}
