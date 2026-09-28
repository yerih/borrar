package com.mivuelto.core.data.network

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.HttpException
import java.io.IOException

/**
 * Formato default de error de Spring Boot (API.md § Convenciones).
 * Ejemplo: {"timestamp":"...","status":401,"error":"Unauthorized",
 *           "message":"Invalid credentials","path":"/auth/app/login"}
 */
data class ErrorResponse(
    @SerializedName("status") val status: Int? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("path") val path: String? = null
)

/**
 * Errores de auth-service § 1 (login POS / app móvil).
 * Mapea 1:1 con la tabla de errores del spec para no filtrar info al usuario
 * más allá de lo que el backend ya revela.
 */
sealed class AuthException(message: String, cause: Throwable? = null) : IOException(message, cause) {
    /** 400 — username/password/terminalSerial vacíos o body inválido. */
    class Validation(message: String) : AuthException(message)

    /** 401 — mensaje genérico "Invalid credentials" (usuario, clave, inactivo o terminal). */
    class InvalidCredentials(message: String = "Invalid credentials") : AuthException(message)

    /** 403 — rol no es ADM/CSH. */
    class RoleNotAllowed(message: String) : AuthException(message)

    /** 423 LOCKED — 3 intentos fallidos, bloqueado 1 hora. */
    class Locked(message: String) : AuthException(message)

    /** Fallos de red / timeout sin respuesta HTTP. */
    class Network(cause: Throwable) : AuthException(cause.message ?: "Network error", cause)

    /** Cualquier otro HTTP (500, etc.) o cuerpo inesperado. */
    class Server(val code: Int, message: String) : AuthException(message)
}

private val gsonLenient = Gson()

/** Extrae [ErrorResponse.message] del errorBody de Retrofit, o null si no es parseable. */
fun HttpException.parseErrorBody(): ErrorResponse? = try {
    response()?.errorBody()?.charStream()?.use { gsonLenient.fromJson(it, ErrorResponse::class.java) }
} catch (_: Exception) {
    null
}

/** Traduce [HttpException] a [AuthException] según API.md § 1. */
fun HttpException.toAuthException(): AuthException {
    val parsed = parseErrorBody()
    val fallback = parsed?.message ?: message()
    return when (code()) {
        400 -> AuthException.Validation(fallback ?: "Invalid request")
        401 -> AuthException.InvalidCredentials(parsed?.message ?: "Invalid credentials")
        403 -> AuthException.RoleNotAllowed(
            parsed?.message ?: "User role is not allowed to log in from the POS app"
        )
        423 -> AuthException.Locked(parsed?.message ?: "User is locked. Try again later.")
        else -> AuthException.Server(code(), fallback ?: "Unexpected error (${code()})")
    }
}

/** Envuelve cualquier [Throwable] de una llamada auth en [AuthException]. */
fun Throwable.asAuthException(): AuthException = when (this) {
    is AuthException -> this
    is HttpException -> toAuthException()
    is IOException -> AuthException.Network(this)
    else -> AuthException.Server(-1, message ?: "Unknown error")
}
