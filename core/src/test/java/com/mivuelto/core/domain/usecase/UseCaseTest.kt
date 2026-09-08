package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.repository.DataRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetDataUseCaseTest {

    private val repository: DataRepository = mockk()
    private val useCase = GetDataUseCase(repository)

    @Test
    fun `invoke calls repository getData`() = runTest {
        coEvery { repository.getData() } returns "test data"
        val result = useCase()
        assertEquals("test data", result)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class GetReportUseCaseTest {

    private val useCase = GetReportUseCase()

    @Test
    fun `invoke returns list of report strings`() = runTest {
        val result = useCase()
        assertEquals(2, result.size)
        assertEquals("Reporte 1", result[0])
        assertEquals("Reporte 2", result[1])
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ProcessPurchaseUseCaseTest {

    private val useCase = ProcessPurchaseUseCase()

    @Test
    fun `invoke returns true for positive amount`() = runTest {
        val result = useCase(100.0)
        assertEquals(true, result)
    }

    @Test
    fun `invoke returns true for small positive amount`() = runTest {
        val result = useCase(0.01)
        assertEquals(true, result)
    }

    @Test
    fun `invoke returns false for zero amount`() = runTest {
        val result = useCase(0.0)
        assertEquals(false, result)
    }

    @Test
    fun `invoke returns false for negative amount`() = runTest {
        val result = useCase(-50.0)
        assertEquals(false, result)
    }
}
