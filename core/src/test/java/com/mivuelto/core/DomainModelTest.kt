package com.mivuelto.core

import com.mivuelto.core.domain.model.BankModel
import com.mivuelto.core.domain.model.CheckPaymentModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DomainModelTest {

    @Test
    fun `CheckPaymentModel default values are correct`() {
        val model = CheckPaymentModel()
        assertNull(model.reference)
        assertNull(model.amount)
        assertNull(model.phone)
        assertNull(model.bank)
    }

    @Test
    fun `CheckPaymentModel can be created with all values`() {
        val bank = BankModel(code = "0105", name = "Mercantil")
        val model = CheckPaymentModel(
            reference = "12345",
            amount = "10000",
            phone = "04121234567",
            bank = bank
        )
        assertEquals("12345", model.reference)
        assertEquals("10000", model.amount)
        assertEquals("04121234567", model.phone)
        assertEquals("0105", model.bank?.code)
        assertEquals("Mercantil", model.bank?.name)
    }

    @Test
    fun `CheckPaymentModel fields are mutable`() {
        val model = CheckPaymentModel()
        model.reference = "NEW_REF"
        model.amount = "5000"
        assertEquals("NEW_REF", model.reference)
        assertEquals("5000", model.amount)
    }

    @Test
    fun `BankModel default values are correct`() {
        val bank = BankModel()
        assertNull(bank.logo)
        assertEquals("", bank.code)
        assertEquals("", bank.name)
    }

    @Test
    fun `BankModel can be created with all values`() {
        val bank = BankModel(logo = 123, code = "0108", name = "Provincial BBVA")
        assertEquals(123, bank.logo)
        assertEquals("0108", bank.code)
        assertEquals("Provincial BBVA", bank.name)
    }

    @Test
    fun `DetailedReportModel default values are correct`() {
        val report = com.mivuelto.core.domain.DetailedReportModel()
        assertEquals(true, report.isSale)
        assertEquals("", report.reference)
        assertEquals(true, report.isApproved)
        assertEquals("", report.pan)
        assertEquals("", report.brand)
        assertEquals("", report.date)
        assertEquals("", report.amount)
    }
}
