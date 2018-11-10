package com.twt.service.push

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.twt.service.R
import com.twt.service.home.HomeNewActivity


/**
 * Created by retrox on 2018/3/19.
 */
class DebugProxyService : Service() {
    val server = DebugProxyServer()
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "debug"
            val channelName = "调试功能"
            val importance = NotificationManager.IMPORTANCE_MIN
            createNotificationChannel(channelId, channelName, importance)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        server.start()
        server.registerRoute(this)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val stackBuilder = TaskStackBuilder.create(this)
        val jumpIntent = Intent()
        jumpIntent.action = "android.intent.action.VIEW";
        val contentUrl = Uri.parse("https://github.com/twtstudio/WePeiYang-Debug-Proxy");
        jumpIntent.data = contentUrl;
        stackBuilder.addParentStack(HomeNewActivity::class.java).addNextIntent(jumpIntent)
        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this, "debug")
                .setContentTitle("微北洋网络调试")
                .setContentText("调试代理服务器已启动 Port: 10086")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(1, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        server.stop()
        super.onDestroy()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}
