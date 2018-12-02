package com.twt.service.home.message

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.igexin.sdk.GTIntentService
import com.igexin.sdk.message.GTCmdMessage
import com.igexin.sdk.message.GTNotificationMessage
import com.igexin.sdk.message.GTTransmitMessage
import com.twt.service.R
import com.twt.service.home.HomeNewActivity

class MessageIntentService : GTIntentService() {
    override fun onReceiveServicePid(context: Context, pid: Int) {}

    override fun onReceiveMessageData(context: Context, msg: GTTransmitMessage) {
    }

    override fun onReceiveClientId(context: Context, clientid: String?) {
        Log.e(GTIntentService.TAG, "onReceiveClientId -> " + "clientid = " + clientid)
    }

    override fun onReceiveOnlineState(context: Context, online: Boolean) {}

    override fun onReceiveCommandResult(context: Context, cmdMessage: GTCmdMessage) {
    }

    override fun onNotificationMessageArrived(context: Context, msg: GTNotificationMessage) {
        val channelID = "GT0"
        val channelName = "微北洋通知"
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
        else null
        val manger = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manger.createNotificationChannel(channel)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(this@MessageIntentService, HomeNewActivity::class.java)
                .apply {
                    putExtra("from", "onNotificationMessageClicked")
                    putExtra("action", "detail")
                }
                , PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, channelID).setContentText(msg.content)
                .setContentTitle(msg.title)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
    }

    override fun onNotificationMessageClicked(context: Context, msg: GTNotificationMessage) {}
}