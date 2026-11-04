package com.mivuelto.core.data.network

import com.mivuelto.core.domain.error.ApiError
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ApiErrorMapperTest {

    private val chargeErrorBody = """
        {
          "timestamp": "2026-11-04T09:00:45.292512184-04:00",
          "status": 422,
          "error": "Unprocessable Content",
          "message": "El cobro fue rechazado: El token venció (120 s); solicite uno nuevo.",
          "path": "/transactions/debin/charge",
          "codigo": "TOKEN_VENCIDO",
          "transactionId": "199e4d85-d2e7-43f3-9f18-95f37f417f0d"
        }
    """.trimIndent()

    @Test
    fun `422 charge error surfaces the backend message as the ApiError message`() {
        val error = httpException(422, chargeErrorBody).toCoreApiError()

        assertTrue(error is ApiError.Unprocessable)
        assertEquals(
            "El cobro fue rechazado: El token venció (120 s); solicite uno nuevo.",
            error.message
        )
    }

    @Test
    fun `error body parses codigo and transactionId`() {
        val parsed = httpException(422, chargeErrorBody).parseErrorBody()

        assertEquals("TOKEN_VENCIDO", parsed?.codigo)
        assertEquals("199e4d85-d2e7-43f3-9f18-95f37f417f0d", parsed?.transactionId)
        assertEquals("/transactions/debin/charge", parsed?.path)
        assertEquals(422, parsed?.status)
    }

    @Test
    fun `blank message falls back to the HTTP exception message`() {
        val body = """{"status": 500, "error": "Internal Server Error", "message": ""}"""

        val error = httpException(500, body).toCoreApiError()

        assertTrue(error is ApiError.Server)
        assertTrue(error.message?.isNotBlank() == true)
    }

    private fun httpException(code: Int, body: String): HttpException =
        HttpException(
            Response.error<Any>(code, body.toResponseBody("application/json".toMediaType()))
        )
}