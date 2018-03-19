package com.twt.service.tjunet.reconnect

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.net.NetworkInfo
import android.util.Log
import com.twt.service.tjunet.api.RealTjuNetService
import com.twt.service.tjunet.ext.fineSSID
import com.twt.service.tjunet.pref.TjuNetPreferences
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.async


/**
 * Created by retrox on 2018/3/18.
 */
class AutoConnectReceiver : BroadcastReceiver() {
    val TAG = "AutoConnectReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            Log.wtf(TAG, "祝您身体健康，再见！")
            return
        }
        if (!TjuNetPreferences.autoConnect) {
            return
        }
        //wifi连接上与否
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {

            val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
            if (info.getState() == NetworkInfo.State.DISCONNECTED) {
                Log.i(TAG, "wifi断开")
            } else if (info.getState() == NetworkInfo.State.CONNECTED) {
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
                wifiManager?.connectionInfo?.let {
                    val ssid = it.ssid.fineSSID
                    if (ssid == "tjuwlan" || ssid == "tjuwlan-lib") {
                        async {
                            val body = RealTjuNetService.login(
                                    username = TjuNetPreferences.username,
                                    password = TjuNetPreferences.password
                            ).awaitAndHandle {
                                Toasty.error(context, "TJU自动登陆失败，${it.message}").show()
                            }
                            body?.let {
                                if (body.data?.startsWith("login_ok") == true) {
                                    Toasty.success(context, "微北洋已为你自动登陆tju").show()
                                }
                            }
                        }
                    }
                }

            }
        }
    }

}