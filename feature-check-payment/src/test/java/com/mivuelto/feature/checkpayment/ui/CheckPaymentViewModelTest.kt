package com.mivuelto.feature.checkpayment.ui

import app.cash.turbine.test
import com.mediosdepago.corpocredit.core.ui_atomics.UiEvent
import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.domain.model.CheckPaymentModel
import com.mivuelto.core.domain.model.Transaction
import com.mivuelto.core.domain.model.TransactionQuery
import com.mivuelto.core.domain.model.TransactionType
import com.mivuelto.core.domain.usecase.QueryTransactionUseCase
import com.mivuelto.feature.checkpayment.ui.invoices.InvoiceModel
import com.mivuelto.feature.checkpayment.ui.navigation.CheckPaymentViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class CheckPaymentViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val queryTransaction: QueryTransactionUseCase = mockk()
    private lateinit var viewModel: CheckPaymentViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CheckPaymentViewModel(queryTransaction)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has null fields`() {
        val state = viewModel.state
        assertEquals(null, state.reference)
        assertEquals(null, state.amount)
        assertEquals(null, state.phone)
        assertEquals(null, state.bank)
    }

    @Test
    fun `updateState replaces the entire state`() {
        val newState = CheckPaymentModel(
            reference = "REF123",
            amount = "5000",
            phone = "04121234567",
            bank = BankModel(code = "0105", name = "Mercantil")
        )
        viewModel.updateState(newState)
        assertEquals("REF123", viewModel.state.reference)
        assertEquals("5000", viewModel.state.amount)
        assertEquals("04121234567", viewModel.state.phone)
        assertEquals("0105", viewModel.state.bank?.code)
    }

    @Test
    fun `getInvoice returns InvoiceModel from current state`() {
        val state = CheckPaymentModel(
            reference = "12345",
            amount = "10000",
            phone = "04121234567",
            bank = BankModel(code = "0108", name = "Provincial BBVA")
        )
        viewModel.updateState(state)
        val invoice = viewModel.getInvoice()
        assertEquals("12345", invoice.ref)
        assertEquals("10000", invoice.amount)
        assertEquals("04121234567", invoice.phone)
        assertEquals("Provincial BBVA", invoice.bank.name)
    }

    @Test
    fun `getInvoice uses default values when state is empty`() {
        val invoice = viewModel.getInvoice()
        assertEquals("empty", invoice.ref)
        assertEquals("empty", invoice.amount)
        assertEquals("empty", invoice.phone)
    }

    @Test
    fun `getInvoice uses empty string for null reference`() {
        viewModel.updateState(CheckPaymentModel(amount = "100"))
        val invoice = viewModel.getInvoice()
        assertEquals("empty", invoice.ref)
    }

    @Test
    fun `getInvoice uses empty string for null amount`() {
        viewModel.updateState(CheckPaymentModel(reference = "REF"))
        val invoice = viewModel.getInvoice()
        assertEquals("empty", invoice.amount)
    }

    @Test
    fun `getInvoice uses empty string for null phone`() {
        viewModel.updateState(CheckPaymentModel(reference = "REF"))
        val invoice = viewModel.getInvoice()
        assertEquals("empty", invoice.phone)
    }

    @Test
    fun `sendPayment maps state to a pago movil query`() = runTest {
        coEvery { queryTransaction(any()) } returns Result.failure(ApiError.Validation("no-op"))
        viewModel.updateState(
            CheckPaymentModel(
                reference = "009281129281",
                amount = "15050",
                phone = "04121234567"
            )
        )

        viewModel.sendPayment()
        testDispatcher.scheduler.advanceUntilIdle()

        val captured = slot<TransactionQuery>()
        coVerify { queryTransaction(capture(captured)) }
        val query = captured.captured
        assertEquals(TransactionType.PAGO_MOVIL, query.transactionType)
        assertEquals(BigDecimal("150.50"), query.amount)
        assertEquals(LocalDate.now(), query.date)
        assertEquals("009281129281", query.reference)
        assertEquals("04121234567", query.phone)
        assertEquals(null, query.document)
        assertEquals(null, query.bankId)
    }

    @Test
    fun `sendPayment emits error event and stores errorMsg when query fails`() = runTest {
        coEvery { queryTransaction(any()) } returns
            Result.failure(ApiError.Validation("amount must be different from 0"))

        viewModel.sendPayment()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effect.test {
            val event = awaitItem()
            assertTrue(event is UiEvent.Error)
            assertEquals("amount must be different from 0", (event as UiEvent.Error).msg)
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals("amount must be different from 0", viewModel.errorMsg)
    }

    @Test
    fun `sendPayment emits success then done and invoice uses verified data`() = runTest {
        val transaction = Transaction(
            id = "tx-1",
            amountCents = 15050,
            transactionTypeId = "type",
            statusId = "status",
            referenceNumber = "009281129281",
            toPhone = "04121234567",
            created = "2026-09-25T09:12:00"
        )
        coEvery { queryTransaction(any()) } returns Result.success(transaction)
        viewModel.updateState(
            CheckPaymentModel(
                reference = "009281129281",
                amount = "15050",
                phone = "04121234567"
            )
        )

        viewModel.sendPayment()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effect.test {
            assertTrue(awaitItem() is UiEvent.OnSuccess)
            assertTrue(awaitItem() is UiEvent.TaskDone)
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals(transaction, viewModel.verifiedTransaction)
        val invoice = viewModel.getInvoice()
        assertEquals("009281129281", invoice.ref)
        assertEquals("150,50", invoice.amount)
        assertEquals("04121234567", invoice.phone)
    }
}

class InvoiceModelTest {

    @Test
    fun `InvoiceModel has correct default values`() {
        val invoice = InvoiceModel()
        assertEquals("comercio", invoice.businessName)
        assertEquals("banco del comercio", invoice.businessBank)
        assertEquals("J-12345678", invoice.businessBankRif)
        assertEquals("localidad o direcci\u00f3n", invoice.address)
        assertEquals("JXXXXXX", invoice.rif)
        assertEquals("0901", invoice.date)
        assertEquals("095959", invoice.time)
        assertEquals("XXXX", invoice.ref)
        assertEquals("XXXX", invoice.phone)
        assertEquals("999999999,99", invoice.amount)
        assertEquals(false, invoice.hasPrinter)
    }

    @Test
    fun `InvoiceModel can be customized`() {
        val bank = BankModel(code = "0105", name = "Mercantil")
        val invoice = InvoiceModel(
            businessName = "Mi Comercio",
            businessBank = "Banesco",
            businessBankRif = "J-87654321",
            bank = bank,
            address = "Av Principal",
            rif = "V12345678",
            date = "0608",
            time = "153000",
            ref = "54321",
            phone = "04129876543",
            amount = "50000,00",
            hasPrinter = true
        )
        assertEquals("Mi Comercio", invoice.businessName)
        assertEquals("Banesco", invoice.businessBank)
        assertEquals("Mercantil", invoice.bank.name)
        assertEquals(true, invoice.hasPrinter)
    }

    @Test
    fun `InvoiceModel fields are mutable`() {
        val invoice = InvoiceModel()
        invoice.rif = "NEW_RIF"
        invoice.ref = "NEW_REF"
        assertEquals("NEW_RIF", invoice.rif)
        assertEquals("NEW_REF", invoice.ref)
    }
}
