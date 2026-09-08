package com.mivuelto.feature.purchase.ui

import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.domain.model.CheckPaymentModel
import com.mivuelto.feature.purchase.ui.invoices.InvoiceModel
import com.mivuelto.feature.purchase.ui.navigation.CheckPaymentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CheckPaymentViewModelTest {

    private lateinit var viewModel: CheckPaymentViewModel

    @Before
    fun setup() {
        viewModel = CheckPaymentViewModel()
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
