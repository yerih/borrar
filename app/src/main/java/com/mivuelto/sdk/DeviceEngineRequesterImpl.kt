package com.mivuelto.sdk

import com.mivuelto.MainApplication
import com.morefun.yapi.engine.DeviceServiceEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import javax.inject.Inject



class DevEngRequesterImpl @Inject constructor(
    private val app: MainApplication
): DeviceEngineRequester {
    private val engine by lazy { runBlocking(Dispatchers.IO){
        while (!app.isEngineInitialized()) delay(100)
        app.sdkEngine!!
    } }
    override suspend fun getEngine(): DeviceServiceEngine = engine

    override suspend fun isMobile(): Boolean = false


}


