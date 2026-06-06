package com.mivuelto.sdk

import com.morefun.yapi.engine.DeviceServiceEngine


interface DeviceEngineRequester{
    suspend fun getEngine(): DeviceServiceEngine
    suspend fun isMobile(): Boolean
}

