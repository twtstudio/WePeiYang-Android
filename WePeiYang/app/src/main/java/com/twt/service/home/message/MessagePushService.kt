package com.twt.service.home.message

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.igexin.sdk.GTServiceManager

class MessagePushService : Service() {
    override fun onCreate() {
        super.onCreate()
        GTServiceManager.getInstance().onCreate(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return GTServiceManager.getInstance().onStartCommand(this, intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return GTServiceManager.getInstance().onBind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        GTServiceManager.getInstance().onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        GTServiceManager.getInstance().onLowMemory()
    }
}