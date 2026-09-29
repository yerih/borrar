package com.mivuelto.core.data.network

import com.mivuelto.core.data.network.requests.LoginRequest
import com.mivuelto.core.data.network.requests.SendChangeRequest
import com.mivuelto.core.data.network.requests.TransactionQueryRequest
import com.mivuelto.core.data.network.requests.UserData
import com.mivuelto.core.data.network.responses.BankResponse
import com.mivuelto.core.data.network.responses.ConfigResponse
import com.mivuelto.core.data.network.responses.DailyStatResponse
import com.mivuelto.core.data.network.responses.LoginResponse
import com.mivuelto.core.data.network.responses.TransactionResponse
import com.mivuelto.core.data.network.responses.TransactionStatsResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class NetworkRequestTest {

    @Test
    fun `LoginRequest contains expected fields`() {
        val request = LoginRequest(
            username = "admin",
            password = "pass123",
            terminalSerial = "MF360-001"
        )
        assertEquals("admin", request.username)
        assertEquals("pass123", request.password)
        assertEquals("MF360-001", request.terminalSerial)
    }

    @Test
    fun `TransactionQueryRequest contains expected fields`() {
        val request = TransactionQueryRequest(
            transactionType = "PAGO_MOVIL",
            reference = "12345",
            amount = BigDecimal("100.50"),
            date = "2026-08-06",
            phone = "04121234567",
            document = "V12345678",
            bankId = "0105"
        )
        assertEquals("PAGO_MOVIL", request.transactionType)
        assertEquals("12345", request.reference)
        assertEquals(0, BigDecimal("100.50").compareTo(request.amount))
        assertEquals("2026-08-06", request.date)
        assertEquals("04121234567", request.phone)
        assertEquals("V12345678", request.document)
        assertEquals("0105", request.bankId)
    }

    @Test
    fun `TransactionQueryRequest optional fields default to null`() {
        val request = TransactionQueryRequest(
            transactionType = "TRANSFERENCIA",
            amount = BigDecimal("50.00"),
            date = "2026-08-06"
        )
        assertEquals(null, request.reference)
        assertEquals(null, request.phone)
        assertEquals(null, request.document)
        assertEquals(null, request.bankId)
    }

    @Test
    fun `SendChangeRequest contains expected fields`() {
        val request = SendChangeRequest(
            amount = BigDecimal("25.00"),
            phone = "04121234567",
            bankId = "bank_dst",
            document = "V123456789",
            sourceBankId = "bank_src"
        )
        assertEquals(0, BigDecimal("25.00").compareTo(request.amount))
        assertEquals("04121234567", request.phone)
        assertEquals("bank_dst", request.bankId)
        assertEquals("V123456789", request.document)
        assertEquals("bank_src", request.sourceBankId)
    }

    @Test
    fun `SendChangeRequest sourceBankId is optional`() {
        val request = SendChangeRequest(
            amount = BigDecimal("25.00"),
            phone = "04121234567",
            bankId = "bank_dst",
            document = "V123456789"
        )
        assertEquals(null, request.sourceBankId)
    }

    @Test
    fun `UserData contains expected fields`() {
        val user = UserData(
            id = "usr_001",
            username = "merchant",
            roleId = "role_admin",
            merchantId = "mch_123"
        )
        assertEquals("usr_001", user.id)
        assertEquals("merchant", user.username)
        assertEquals("role_admin", user.roleId)
        assertEquals("mch_123", user.merchantId)
    }
}

class NetworkResponseTest {

    @Test
    fun `LoginResponse contains expected fields`() {
        val user = UserData("1", "admin", "role1", "mch1")
        val response = LoginResponse(
            sessionToken = "abc123token",
            expiresAt = "2026-12-31",
            user = user
        )
        assertEquals("abc123token", response.sessionToken)
        assertEquals("2026-12-31", response.expiresAt)
        assertEquals("admin", response.user.username)
    }

    @Test
    fun `BankResponse contains expected fields`() {
        val response = BankResponse(
            id = "bank_1",
            code = "0105",
            name = "Mercantil",
            apiActive = true
        )
        assertEquals("bank_1", response.id)
        assertEquals("0105", response.code)
        assertEquals("Mercantil", response.name)
        assertEquals(true, response.apiActive)
    }

    @Test
    fun `ConfigResponse contains expected fields`() {
        val response = ConfigResponse(
            key = "timeout",
            value = "30"
        )
        assertEquals("timeout", response.key)
        assertEquals("30", response.value)
    }

    @Test
    fun `TransactionResponse contains expected fields`() {
        val response = TransactionResponse(
            id = "txn_001",
            transactionType = "PAYMENT",
            amount = BigDecimal("100.50"),
            status = "APPROVED",
            referenceNumber = "REF123",
            created = "2026-08-06T12:00:00Z"
        )
        assertEquals("txn_001", response.id)
        assertEquals("PAYMENT", response.transactionType)
        assertEquals(0, BigDecimal("100.50").compareTo(response.amount))
        assertEquals("APPROVED", response.status)
        assertEquals("REF123", response.referenceNumber)
        assertEquals("2026-08-06T12:00:00Z", response.created)
    }

    @Test
    fun `TransactionResponse optional fields default correctly`() {
        val response = TransactionResponse(
            id = "txn_001",
            transactionType = "PAYMENT",
            amount = BigDecimal("100.50"),
            status = "APPROVED",
            referenceNumber = "REF123",
            created = "2026-08-06T12:00:00Z"
        )
        assertEquals(null, response.cashRegisterId)
        assertEquals(null, response.toPhone)
        assertEquals(null, response.fromPhone)
        assertEquals(null, response.response)
        assertEquals(null, response.isValidated)
        assertEquals(null, response.updated)
        assertEquals(null, response.sessionId)
        assertEquals(null, response.idempotencyKey)
        assertEquals(null, response.commerceId)
    }

    @Test
    fun `TransactionStatsResponse contains expected fields`() {
        val response = TransactionStatsResponse(
            totalAmount = BigDecimal("4230.75"),
            totalCount = 38,
            chartData = listOf(
                DailyStatResponse("2026-09-01", BigDecimal("120.00"), 2),
                DailyStatResponse("2026-09-02", BigDecimal("0.00"), 0)
            )
        )
        assertEquals(0, BigDecimal("4230.75").compareTo(response.totalAmount))
        assertEquals(38, response.totalCount)
        assertEquals(2, response.chartData.size)
        assertEquals("2026-09-01", response.chartData[0].date)
        assertEquals(0, BigDecimal("120.00").compareTo(response.chartData[0].amount))
        assertEquals(2, response.chartData[0].count)
    }
}
