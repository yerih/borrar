package com.mivuelto.core.domain.repository

import com.mivuelto.core.domain.model.SendChangeCommand
import com.mivuelto.core.domain.model.Transaction
import com.mivuelto.core.domain.model.TransactionQuery
import com.mivuelto.core.domain.model.TransactionStats
import java.time.LocalDate

/**
 * core-service — Motor transaccional (API.md § 2).
 * Implementación en `:core-data`. Requiere el token de auth-service: el
 * repositorio toma el header `Authorization` de la sesión activa, por lo que
 * estas operaciones lanzan [com.mivuelto.core.data.network.AuthException.InvalidCredentials]
 * si no hay sesión y [com.mivuelto.core.data.network.TransactionException]
 * en los demás casos (404 no encontrado, 422 banco sin API, 501 send-change...).
 */
interface TransactionRepository {

    /** `POST /transactions/query` — verifica el pago o transferencia. */
    suspend fun queryTransaction(query: TransactionQuery): Transaction

    /**
     * `POST /transactions/send-change` — hoy siempre termina en
     * `TransactionException.NotImplemented` (501) tras pasar las validaciones.
     */
    suspend fun sendChange(command: SendChangeCommand)

    /** `GET /transactions/history` — últimos 20 vueltos del comercio, sin filtros. */
    suspend fun getHistory(): List<Transaction>

    /** `GET /transactions/stats?startDate=&endDate=` — rango máximo 31 días. */
    suspend fun getStats(startDate: LocalDate, endDate: LocalDate): TransactionStats
}
