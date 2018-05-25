package com.twt.service.tjunet.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.twt.service.tjunet.api.RealTjuNetService
import com.twt.service.tjunet.ext.fineSSID
import com.twt.service.tjunet.ext.stringIP
import com.twt.service.tjunet.pref.TjuNetPreferences
import com.twt.service.tjunet.view.TjuNetActivity
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException


data class TjuNetStatus(val ssid: String, val connected: Boolean, val ip: String, val message: String)
object TjuNetViewModel {
    private val connectivityManager = CommonContext.application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    private val wifiManager = CommonContext.application.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager

    private val statusLiveData = object : MutableLiveData<TjuNetStatus>() {
        override fun onActive() {
            super.onActive()
            refreshNetworkInfo()
        }
    }

    val liveStatus: LiveData<TjuNetStatus> get() = statusLiveData

    fun refreshNetworkInfo() {
        val networkInfo = connectivityManager?.activeNetworkInfo
        if (networkInfo == null) {
            statusLiveData.postValue(TjuNetStatus("SSID获取出错", false, "IP获取出错", "Not logged in"))
            return
        }
        when (networkInfo.type) {
            ConnectivityManager.TYPE_MOBILE -> {
                statusLiveData.postValue(TjuNetStatus("数据网络", false, getMobileIpAddress(), "Not Connected"))
            }
            ConnectivityManager.TYPE_WIFI -> {
                val connectionInfo = wifiManager?.connectionInfo
                connectionInfo?.let {
                    val ssid = it.ssid.fineSSID
                    val ip = it.ipAddress.stringIP
//                    refreshLoginState()
                    statusLiveData.postValue(TjuNetStatus(ssid, false, ip, "Checking Connect Status.."))
                    async(UI) {
                        val result = RealTjuNetService.getStatus().await()
                        result.data?.let {
                            if (it.online) {
                                val connected = true
                                val text = "Connected"
                                statusLiveData.postValue(TjuNetStatus(ssid, connected, ip, text))
                            } else {
                                val connected = false
                                val text = "Not Connected"
                                statusLiveData.postValue(TjuNetStatus(ssid, connected, ip, text))
                            }
                        }
                    }.invokeOnCompletion {
                        it?.let {
                            it.printStackTrace()
                            statusLiveData.postValue(TjuNetStatus(ssid, false, ip, "Error occurred"))
                        }
                    }
                }
            }
        }
    }

    fun login(context: Context) {
        launch(UI + QuietCoroutineExceptionHandler) {
            if (TjuNetPreferences.password == "") {
                Toasty.info(context, "请前往上网功能页面登陆以保存您的密码").show()
                val intent = Intent(context, TjuNetActivity::class.java)
                context.startActivity(intent)
                return@launch
            }
            val body = RealTjuNetService.login(
                    username = TjuNetPreferences.username,
                    password = TjuNetPreferences.password
            ).awaitAndHandle {
                statusLiveData.repost {
                    copy(connected = false, message = "好像出了什么问题 ${it.message}")
                }
            }
            body?.let {
                if (body.data?.startsWith("login_ok") == true) {
                    refreshNetworkInfo()
                }
            }
        }
    }

    fun logout(context: Context) {
        launch(UI + QuietCoroutineExceptionHandler) {
            val body = RealTjuNetService.logoutTry(
                    username = TjuNetPreferences.username,
                    password = TjuNetPreferences.password
            ).awaitAndHandle {
                statusLiveData.repost {
                    copy(connected = false, message = "好像出了什么问题 ${it.message}")
                }
            }
            body?.let {
                refreshNetworkInfo()
            }
        }
    }

    private fun getMobileIpAddress(): String {
        try {
            //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.getInetAddresses()
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress() && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return "IP获取错误"
    }

    fun <T> MutableLiveData<T>.repost(init: T.() -> T) {
        val value = this.value
        val res = value?.init()
        res?.let {
            this.postValue(it)
        }
    }

}