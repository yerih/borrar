package com.mivuelto.core.data.network

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.mivuelto.core.domain.error.ApiError
import retrofit2.HttpException
import java.io.IOException

/**
 * Formato default de error de Spring Boot (API.md § Convenciones), tal como
 * aparece en `/transactions/debin/charge` (API.md § 2):
 * {"timestamp":"...","status":422,"error":"Unprocessable Content",
 *  "message":"El cobro fue rechazado: ...","path":"/transactions/debin/charge",
 *  "codigo":"TOKEN_VENCIDO","transactionId":"..."}
 *
 * [message] es el texto que se muestra en la pantalla de error.
 */
data class ErrorResponse(
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("status") val status: Int? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("path") val path: String? = null,
    @SerializedName("codigo") val codigo: String? = null,
    @SerializedName("transactionId") val transactionId: String? = null
)

private val gson = Gson()

/** Extrae [ErrorResponse] del errorBody de Retrofit, o null si no es parseable. */
fun HttpException.parseErrorBody(): ErrorResponse? = try {
    response()?.errorBody()?.charStream()?.use { gson.fromJson(it, ErrorResponse::class.java) }
} catch (_: Exception) {
    null
}

/**
 * Mapea un fallo HTTP de core-service (API.md § 2) a [ApiError].
 * El único código con semántica distinta entre servicios es el 403, que aquí
 * es comercio inactivo o monto fuera de límite.
 */
fun HttpException.toCoreApiError(): ApiError = toApiError(
    forbidden = { ApiError.CommerceInactive(it) }
)

/**
 * Mapea un fallo HTTP de auth-service (API.md § 1) a [ApiError].
 * 403 aquí es rol no permitido para loguear desde la app POS.
 */
fun HttpException.toAuthApiError(): ApiError = toApiError(
    forbidden = { ApiError.RoleNotAllowed(it) }
)

private fun HttpException.toApiError(forbidden: (String) -> ApiError): ApiError {
    val parsed = parseErrorBody()
    val message = parsed?.message?.takeIf { it.isNotBlank() } ?: this.message()
    return when (code()) {
        400 -> ApiError.Validation(message ?: "Invalid request")
        401 -> ApiError.Unauthorized(message ?: "Invalid credentials")
        403 -> forbidden(message ?: "Forbidden")
        422 -> ApiError.Unprocessable(message ?: "Bank integration is not active")
        423 -> ApiError.Locked(message ?: "User is locked. Try again later.")
        501 -> ApiError.NotImplemented(message ?: "Not implemented yet")
        502 -> ApiError.BankCommunication(message ?: "Bank communication error")
        503 -> ApiError.BankUnavailable(message ?: "Bank is unavailable")
        504 -> ApiError.GatewayTimeout(message ?: "Gateway timeout")
        else -> ApiError.Server(code(), message ?: "Unexpected error (${code()})")
    }
}

fun Throwable.toCoreApiError(): ApiError = when (this) {
    is ApiError -> this
    is HttpException -> toCoreApiError()
    is IOException -> ApiError.Network(this)
    else -> ApiError.Server(-1, message ?: "Unknown error")
}

fun Throwable.toAuthApiError(): ApiError = when (this) {
    is ApiError -> this
    is HttpException -> toAuthApiError()
    is IOException -> ApiError.Network(this)
    else -> ApiError.Server(-1, message ?: "Unknown error")
}
