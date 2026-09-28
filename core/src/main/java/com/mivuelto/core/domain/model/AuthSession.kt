package com.mivuelto.core.domain.model

/**
 * Sesión POS abierta con POST /auth/app/login (API.md § 1).
 * @param sessionToken JWT (`sub=userId`) que consume core-service como
 * `Authorization: Bearer <sessionToken>`.
 * @param expiresAt LocalDateTime del backend (`2026-09-25T15:30:00`, sin zona).
 */
data class AuthSession(
    val sessionToken: String,
    val expiresAt: String,
    val user: AuthUser
)
