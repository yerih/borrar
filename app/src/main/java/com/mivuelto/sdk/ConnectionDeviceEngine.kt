package com.mivuelto.sdk

import android.app.Application
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.morefun.yapi.engine.DeviceServiceEngine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Application.connectToService(onDisconnect: ()->Unit = {}): DeviceServiceEngine = suspendCoroutine { continuation ->
    val intent = Intent().apply {
        action = "com.morefun.ysdk.service"
        setPackage("com.morefun.ysdk")
    }
    val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            var engine = DeviceServiceEngine.Stub.asInterface(service)
            linkToDeath(service){
                engine = null
                bindService(intent, this, BIND_AUTO_CREATE)
            }
            continuation.resume(engine)
        }
        override fun onServiceDisconnected(p0: ComponentName?) = onDisconnect()
    }
    bindService(intent, connection, BIND_AUTO_CREATE)
}

fun Application.disconnectService() = unbindService(object : ServiceConnection {
    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) = Unit
    override fun onServiceDisconnected(p0: ComponentName?) = Unit
})


private fun linkToDeath(service: IBinder?, onBinderDead: ()->Unit = {}) {
    try {
        service?.linkToDeath(onBinderDead, 0)
    } catch (e: RemoteException) {
        e.printStackTrace()
    }
}



