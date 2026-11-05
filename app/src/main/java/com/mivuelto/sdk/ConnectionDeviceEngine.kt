package com.mivuelto.sdk

import android.app.Application
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.morefun.yapi.engine.DeviceServiceEngine
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/** Conexión activa con el servicio YSDK; se usa para desenlazar de forma segura. */
private var boundConnection: ServiceConnection? = null

suspend fun Application.connectToService(onDisconnect: () -> Unit = {}): DeviceServiceEngine? =
    suspendCancellableCoroutine { continuation ->
        val intent = Intent().apply {
            action = "com.morefun.ysdk.service"
            setPackage("com.morefun.ysdk")
        }
        val connection = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
                // onServiceConnected puede volver a llegar tras un re-enlace del
                // linkToDeath: reanudar de nuevo revienta el proceso.
                if (!continuation.isActive) return
                var engine = DeviceServiceEngine.Stub.asInterface(service)
                linkToDeath(service) {
                    engine = null
                    bindService(intent, this, BIND_AUTO_CREATE)
                }
                continuation.resume(engine)
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                // onServiceDisconnected llega también cuando el servicio muere con
                // la app en segundo plano; reanudar una continuación ya resuelta
                // lanza IllegalStateException y mataba el proceso — al volver, el
                // sistema recreaba la actividad sin savedInstanceState y la
                // navegación perdía el back stack (se volvía a la primera pantalla).
                if (continuation.isActive) {
                    continuation.resume(null)
                }
                onDisconnect()
            }
        }
        boundConnection = connection
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

/**
 * Desenlaza la conexión registrada en [connectToService]. Idempotente: unbind
 * con una instancia distinta de la que se enlazó lanzaba
 * `IllegalArgumentException: Service not registered` y mataba el proceso.
 */
fun Application.disconnectService() {
    val connection = boundConnection ?: return
    boundConnection = null
    try {
        unbindService(connection)
    } catch (_: IllegalArgumentException) {
        // Ya estaba desenlazado.
    }
}


private fun linkToDeath(service: IBinder?, onBinderDead: () -> Unit = {}) {
    try {
        service?.linkToDeath(onBinderDead, 0)
    } catch (e: RemoteException) {
        e.printStackTrace()
    }
}