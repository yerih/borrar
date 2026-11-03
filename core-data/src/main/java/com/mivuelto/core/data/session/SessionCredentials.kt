package com.mivuelto.core.data.session

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Credenciales de login en memoria (solo vida del proceso) para poder
 * re-loguear de forma silenciosa antes de cada llamada transaccional.
 *
 * Se guardan en cada login exitoso y **nunca se persisten a disco** — si la
 * muere el proceso el usuario debe loguearse de nuevo en la pantalla LOGIN.
 */
@Singleton
class SessionCredentials @Inject constructor() {

    private var credentials: Credentials? = null

    fun save(username: String, password: String, terminalSerial: String) {
        credentials = Credentials(username, password, terminalSerial)
    }

    fun get(): Credentials? = credentials

    fun clear() {
        credentials = null
    }

    data class Credentials(
        val username: String,
        val password: String,
        val terminalSerial: String
    )
}