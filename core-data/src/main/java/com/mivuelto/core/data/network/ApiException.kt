package com.mivuelto.core.data.network

import java.io.IOException

/**
 * Errores de la capa de red, con el `message` del body de Spring Boot ya
 * resuelto. Subtipos por microservicio: [AuthException] (auth-service, § 1) y
 * [TransactionException] (core-service, § 2).
 *
 * Existe este supertype para que la UI tenga un único punto de catch: un 401
 * de cualquier servicio significa "sesión inválida o expirada" y se re-loguea
 * igual, sin importar de dónde venga.
 */
sealed class ApiException(message: String, cause: Throwable? = null) :
    IOException(message, cause)
