package com.mivuelto.core.data.network

import retrofit2.HttpException
import java.io.IOException

/**
 * Errores de core-service (API.md § 2). El subconjunto común entre
 * `query`, `send-change`, `history` y `stats` está en [AuthException];
 * aquí viven los códigos propios del motor transaccional.
 */
sealed class TransactionException(message: String, cause: Throwable? = null) :
    ApiException(message, cause) {

    /** 400 — body/query inválido (tipo ausente, amount = 0, `document` faltante en TRANSFERENCIA, `bankId` requerido y ausente, fecha inválida). */
    class Validation(message: String) : TransactionException(message)

    /** 403 — comercio inactivo, o el monto excede `config_users.tx_amount_limit` (send-change). */
    class CommerceInactive(message: String) : TransactionException(message)

    /** 404 — pago/transferencia no encontrado (ni local ni en el banco), o banco no encontrado. */
    class NotFound(message: String) : TransactionException(message)

    /** 422 — el banco no tiene integración API activa, o (TRANSFERENCIA) la cuenta no tiene `accountNumber`. */
    class Unprocessable(message: String) : TransactionException(message)

    /**
     * 501 — `send-change` no está implementado en el backend (respuesta **esperada**
     * hoy, no es un fallo transitorio: no reintentar).
     */
    class NotImplemented(message: String) : TransactionException(message)

    /** 502 — error de autenticación/comunicación/respuesta inválida del banco. */
    class BankCommunication(message: String) : TransactionException(message)

    /** 503 — banco no disponible / circuit breaker abierto. */
    class BankUnavailable(message: String) : TransactionException(message)

    /** 504 — timeout esperando a banking-router-service. */
    class GatewayTimeout(message: String) : TransactionException(message)

    /** 500 — inconsistencia interna: el router confirmó pero no se pudo releer el registro local. */
    class Server(message: String) : TransactionException(message)

    /** Fallos de red / timeout sin respuesta HTTP. */
    class Network(cause: Throwable) :
        TransactionException(cause.message ?: "Network error", cause)
}

/**
 * Traduce [HttpException] a [ApiException] según las tablas de § 2.
 * Un 401 (token vencido o ausente) reutiliza [AuthException.InvalidCredentials]
 * por compatibilidad con el flujo de sesión: la UI re-loguea igual.
 */
fun HttpException.toTransactionException(): ApiException {
    val parsed = parseErrorBody()
    val fallback = parsed?.message ?: message()
    return when (code()) {
        400 -> TransactionException.Validation(fallback ?: "Invalid request")
        401 -> AuthException.InvalidCredentials(parsed?.message ?: "Invalid credentials")
        403 -> TransactionException.CommerceInactive(
            parsed?.message ?: "Commerce is inactive"
        )
        404 -> TransactionException.NotFound(
            parsed?.message ?: "Transaction not found"
        )
        422 -> TransactionException.Unprocessable(
            parsed?.message ?: "Bank integration is not active"
        )
        501 -> TransactionException.NotImplemented(
            parsed?.message ?: "Sending change is not implemented yet"
        )
        502 -> TransactionException.BankCommunication(
            parsed?.message ?: "Bank communication error"
        )
        503 -> TransactionException.BankUnavailable(
            parsed?.message ?: "Bank is unavailable"
        )
        504 -> TransactionException.GatewayTimeout(
            parsed?.message ?: "Gateway timeout"
        )
        else -> TransactionException.Server(fallback ?: "Unexpected error (${code()})")
    }
}

/** Envuelve cualquier [Throwable] de una llamada a core-service en [ApiException]. */
fun Throwable.asTransactionException(): ApiException = when (this) {
    is TransactionException -> this
    is AuthException -> throw this
    is HttpException -> toTransactionException()
    is IOException -> TransactionException.Network(this)
    else -> TransactionException.Server(message ?: "Unknown error")
}
