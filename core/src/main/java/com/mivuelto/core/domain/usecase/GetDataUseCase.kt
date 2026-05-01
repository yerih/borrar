package com.mivuelto.core.domain.usecase

import com.mivuelto.core.domain.repository.DataRepository
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    private val repository: DataRepository
) {
    suspend operator fun invoke(): Any {
        return repository.getData()
    }
}
