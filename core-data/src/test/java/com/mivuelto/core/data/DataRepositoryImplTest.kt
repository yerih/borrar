package com.mivuelto.core.data

import com.mivuelto.core.data.datasource.RemoteDataSource
import com.mivuelto.core.data.repository.DataRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DataRepositoryImplTest {

    private val remoteDataSource: RemoteDataSource = mockk()
    private val repository = DataRepositoryImpl(remoteDataSource)

    @Test
    fun `getData delegates to remoteDataSource fetchData`() = runTest {
        coEvery { remoteDataSource.fetchData() } returns Unit
        val result = repository.getData()
        assertEquals(Unit, result)
    }
}
