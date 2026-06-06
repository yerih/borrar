package com.mivuelto

import android.app.Application
import android.os.Build
import com.mivuelto.core.ui.launch
import com.mivuelto.sdk.connectToService
import com.mivuelto.sdk.disconnectService
import com.morefun.yapi.engine.DeviceInfoConstrants
import com.morefun.yapi.engine.DeviceServiceEngine
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers

@HiltAndroidApp
class MainApplication : Application() {
    var sdkEngine: DeviceServiceEngine? = null
    lateinit var serialNum: String
    private var hasPrinter = false

    override fun onCreate() {
        super.onCreate()
        serialNum = ""
        when(Build.MODEL){
            "MF360", "MF919" -> launch(Dispatchers.IO){
                sdkEngine = connectToService(onDisconnect = ::disconnectService)
                serialNum = sdkEngine?.devInfo?.getString(DeviceInfoConstrants.COMMOM_SN)?:toString()
            }
            else -> serialNum = "97220811789034"
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        disconnectService()
    }

    fun isEngineInitialized() = sdkEngine != null

    fun hasPrinter() = hasPrinter

}
