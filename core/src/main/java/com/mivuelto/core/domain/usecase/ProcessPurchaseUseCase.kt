package com.mivuelto.core.domain.usecase

import javax.inject.Inject

class ProcessPurchaseUseCase @Inject constructor() {
    suspend operator fun invoke(amount: Double): Boolean {
        // Lógica de negocio para procesar una compra
        return amount > 0
    }
}
