package com.twt.service.slide.flash

import android.annotation.TargetApi
import android.app.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import com.twt.service.slide.R

class CoreSlideService : Service() {
    val TAG = "CoreService"
    val flashFloatWindow2 by lazy {
        FlashFloatWindow2(this.applicationContext)
    }
    lateinit var alarmPendingIntent: PendingIntent
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel("WebPageNoti", "WebPageSync", NotificationManager.IMPORTANCE_LOW)
//            createNotificationChannel("ClipBoardSync", "ClipBoardSync", NotificationManager.IMPORTANCE_LOW)
            createNotificationChannel("ServiceStatus", "HandOffStatus", NotificationManager.IMPORTANCE_MIN)
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        postStatusNoti("HandOff 正在运行", true)
        try {
            flashFloatWindow2.showFloatWindow()
        } catch (e: Exception) {
            Toast.makeText(this, "您需要授权微北洋使用悬浮窗权限 然后重启APP使用", Toast.LENGTH_SHORT).show()
                    openSetting(this)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun postStatusNoti(message: String, status: Boolean) {
//        val refreshIntent = Intent(this, SocketControlReceiver::class.java).apply {
//            action = SocketControlReceiver.ACTION_REFRESH
//        }
//        val refreshPendingIntent = PendingIntent.getBroadcast(this, 0, refreshIntent, 0)
//
//        val stopIntent = Intent(this, SocketControlReceiver::class.java).apply {
//            action = SocketControlReceiver.ACTION_SHUTDOWN
//        }
//        val stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0)

        val notification = NotificationCompat.Builder(this, "ServiceStatus")
                .setContentTitle("微北洋课程表服务")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setWhen(System.currentTimeMillis())
                .setColor(Color.parseColor("#42b983"))
//                .setColorized(true)
                .setSmallIcon(R.drawable.ic_face)
                .build()
        startForeground(2, notification)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * 跳转到悬浮窗权限的地方
     */
    fun openSetting(context: Context) {
        try {
            val localIntent = Intent(
                    "miui.intent.action.APP_PERM_EDITOR")
            localIntent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity")
            localIntent.putExtra("extra_pkgname", context.getPackageName())
            context.startActivity(localIntent)
        } catch (localActivityNotFoundException: ActivityNotFoundException) {
            val intent1 = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.getPackageName(), null)
            intent1.setData(uri)
            context.startActivity(intent1)
        }
    }
}