package com.twt.service.push

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created by retrox on 2018/3/19.
 */
class DebugProxyService: Service() {
    val server = DebugProxyServer()
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        server.start()
        return START_STICKY
    }

    override fun onDestroy() {
        server.stop()
        super.onDestroy()
    }
}