package com.mivuelto.core.data.network.requests

import com.google.gson.annotations.SerializedName

/**
 * `user` dentro de POST /auth/app/login 200 (API.md § 1).
 * `roleId` es el **id** del rol (no el slug); login POS solo admite ADM/CSH.
 * Nota spec: el JSON trae `merchantId` (id del comercio).
 */
data class UserData(
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("roleId")
    val roleId: String,
    @SerializedName("merchantId")
    val merchantId: String
)

