package com.mivuelto.core.domain.model

/**
 * Usuario autenticado vía auth-service (API.md § 1).
 * `roleId` es el id del rol; POS solo admite ADM/CSH.
 * `merchantId` = id del comercio (el JWT lo trae como claim `commerceId`).
 */
data class AuthUser(
    val id: String,
    val username: String,
    val roleId: String,
    val merchantId: String
)
