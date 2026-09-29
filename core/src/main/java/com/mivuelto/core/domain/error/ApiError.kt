package com.mivuelto.core.domain.error

/**
 * Error de API como payload de `Result.failure` (nunca se lanza entre capas).
 *
 * Extiende [Throwable] porque `Result<T>` de Kotlin solo admite fallas de ese
 * tipo. La taxonomía fusiona las tablas de API.md § 1 (auth-service) y § 2
 * (core-service) y los errores que el cliente detecta sin red.
 *
 * La distinción que importa en UI:
 * - [Unauthorized] → re-loguear (401 del servidor o ausencia de sesión local)
 * - [Validation] → mostrar el detalle, el usuario puede corregir
 * - [NotImplemented] → la acción no existe aún (501 de send-change); no reintentar
 * - [Locked] → 3 intentos fallidos, esperar 1 hora
 */
sealed class ApiError(message: String, cause: Throwable? = null) :
    Throwable(message, cause) {

    /** 400 — body o query inválido; también validación hecha en el cliente. */
    class Validation(message: String) : ApiError(message)

    /** 401 — servidor; o ausencia de sesión local. Significa re-loguear. */
    class Unauthorized(message: String = "Invalid credentials") : ApiError(message)

    /** 403 auth-service — rol no permitido para loguear desde la app POS. */
    class RoleNotAllowed(message: String) : ApiError(message)

    /** 423 LOCKED — 3 intentos acumulados, bloqueado 1 hora. */
    class Locked(message: String) : ApiError(message)

    /** 403 core-service — comercio inactivo, o monto sobre el límite. */
    class CommerceInactive(message: String) : ApiError(message)

    /** 404 — recurso no encontrado (transacción ni local ni en banco, banco). */
    class NotFound(message: String) : ApiError(message)

    /** 422 — banco sin integración API activa, o cuenta sin `accountNumber`. */
    class Unprocessable(message: String) : ApiError(message)

    /** 501 — `send-change` sin implementar en el backend. No reintentar. */
    class NotImplemented(message: String) : ApiError(message)

    /** 502 — error de comunicación/autenticación con el banco. */
    class BankCommunication(message: String) : ApiError(message)

    /** 503 — banco no disponible (circuit breaker abierto). */
    class BankUnavailable(message: String) : ApiError(message)

    /** 504 — timeout esperando a banking-router-service. */
    class GatewayTimeout(message: String) : ApiError(message)

    /** 500 u otro código no previsto; `-1` cuando no hubo respuesta HTTP. */
    class Server(val code: Int, message: String) : ApiError(message)

    /** Fallo de red o timeout sin respuesta HTTP. */
    class Network(cause: Throwable) : ApiError(cause.message ?: "Network error", cause)
}
