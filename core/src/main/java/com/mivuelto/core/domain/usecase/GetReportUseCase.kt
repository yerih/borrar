package com.mivuelto.core.domain.usecase

import javax.inject.Inject

class GetReportUseCase @Inject constructor() {
    suspend operator fun invoke(): List<String> {
        // Lógica para obtener reportes
        return listOf("Reporte 1", "Reporte 2")
    }
}
