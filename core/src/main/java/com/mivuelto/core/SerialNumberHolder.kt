package com.mivuelto.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SerialNumberHolder @Inject constructor() {
    private val _serialNumber = MutableStateFlow<String?>(null)
    val serialNumber: StateFlow<String?> = _serialNumber.asStateFlow()

    fun setSerialNumber(value: String) {
        _serialNumber.value = value
    }

}