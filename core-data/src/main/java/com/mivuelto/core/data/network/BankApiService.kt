package com.mivuelto.core.data.network

import com.mivuelto.core.data.network.responses.BankResponse
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Catálogo de bancos — API.md § 7. Ruta sin prefijo `/backoffice`. Requiere
 * `Authorization: Bearer <token de auth-service>` (token de sesión del login
 * de la app).
 */
interface BankApiService {

    /**
     * GET /banks — bancos con `active = true`, cacheado y ordenado por nombre.
     * Sin parámetro `apiActive`: listado completo (la app móvil debe mostrar
     * cualquier banco venezolano como destino/origen válido).
     */
    @GET("banks")
    suspend fun getBanks(
        @Header("Authorization") authorization: String
    ): List<BankResponse>
}
