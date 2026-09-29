package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.model.TransactionQuery
import com.mivuelto.core.domain.model.TransactionType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class QueryTransactionUseCaseTest {

    private val repository = mockk<com.mivuelto.core.domain.repository.TransactionRepository>(relaxed = true)
    private val useCase = QueryTransactionUseCase(repository)

    private val captured = slot<TransactionQuery>()

    @Test
    fun `PAGO_MOVIL forwards the query and keeps phone`() = runTest {
        val query = baseQuery().copy(phone = "04121234567")
        coEvery { repository.queryTransaction(any()) } returns Result.success(mockk())

        useCase(query)

        coVerify { repository.queryTransaction(capture(captured)) }
        assertEquals("04121234567", captured.captured.phone)
    }

    @Test
    fun `TRANSFERENCIA drops phone because the backend ignores it`() = runTest {
        val query = baseQuery().copy(
            transactionType = TransactionType.TRANSFERENCIA,
            document = "J013759368",
            phone = "04121234567"
        )
        coEvery { repository.queryTransaction(any()) } returns Result.success(mockk())

        useCase(query)

        coVerify { repository.queryTransaction(capture(captured)) }
        assertEquals(null, captured.captured.phone)
    }

    @Test
    fun `TRANSFERENCIA without document is rejected`() = runTest {
        val query = baseQuery().copy(transactionType = TransactionType.TRANSFERENCIA, document = null)

        val result = useCase(query)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApiError.Validation)
    }

    @Test
    fun `zero amount is rejected`() = runTest {
        val query = baseQuery().copy(amount = BigDecimal.ZERO)

        val result = useCase(query)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApiError.Validation)
    }

    @Test
    fun `reference shorter than six digits is rejected`() = runTest {
        val query = baseQuery().copy(reference = "12345")

        val result = useCase(query)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApiError.Validation)
    }

    @Test
    fun `non numeric reference is rejected`() = runTest {
        val query = baseQuery().copy(reference = "12345A")

        val result = useCase(query)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is ApiError.Validation)
    }

    @Test
    fun `null reference is allowed`() = runTest {
        val query = baseQuery().copy(reference = null)
        coEvery { repository.queryTransaction(any()) } returns Result.success(mockk())

        useCase(query)

        coVerify { repository.queryTransaction(any()) }
    }

    private fun baseQuery() = TransactionQuery(
        transactionType = TransactionType.PAGO_MOVIL,
        amount = BigDecimal("150.50"),
        date = LocalDate.of(2026, 9, 25),
        reference = "009281129281"
    )
}
