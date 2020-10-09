package com.twt.service.tjunet.view

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.twt.service.tjunet.R
import com.twt.service.tjunet.api.RealTjuNetService
import com.twt.service.tjunet.ext.fineSSID
import com.twt.service.tjunet.ext.stringIP
import com.twt.service.tjunet.pref.TjuNetPreferences
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.textColor
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException


/**
 * Created by retrox on 2018/3/18.
 */
class TjuNetViewComponent(itemView: View, private val owner: LifecycleOwner) : RecyclerView.ViewHolder(itemView) {

    private val context = itemView.context
    private val wlanName: TextView = itemView.findViewById(R.id.text_wlan)
    private val ipText: TextView = itemView.findViewById(R.id.text_ip)
    private val status: TextView = itemView.findViewById(R.id.text_status)
    private val refreshBtn: Button = itemView.findViewById(R.id.btn_refresh)
    private val loginBtn: Button = itemView.findViewById(R.id.btn_connect)
    private val logoutBtn: Button = itemView.findViewById(R.id.btn_disconnect)
    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
    private val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    private fun refreshNetworkInfo() {
        val networkInfo = connectivityManager?.activeNetworkInfo
        if (networkInfo == null) {
            wlanName.text = "无网络连接"
            ipText.text = "IP获取出错"
            status.text = "未登陆"
            return
        }
        when (networkInfo.type) {
            ConnectivityManager.TYPE_MOBILE -> {
                wlanName.text = "数据网络"
                ipText.text = getMobileIpAddress()
                status.text = "未连接"
                logoutBtn.setClickableState(false)
                loginBtn.setClickableState(false)
            }
            ConnectivityManager.TYPE_WIFI -> {
                val connectionInfo = wifiManager?.connectionInfo
                connectionInfo?.let {
                    wlanName.text = it.ssid.fineSSID
                    ipText.text = it.ipAddress.stringIP
                    refreshLoginState()
                    logoutBtn.setClickableState(true)
                    loginBtn.setClickableState(true)
                }
            }
        }
    }

    private fun refreshLoginState() {
        GlobalScope.async(Dispatchers.Main) {
            val result = RealTjuNetService.getStatus().awaitAndHandle {
                Toasty.error(this@TjuNetViewComponent.context, "好像出了什么问题，${it.message}").show()
            }
            result?.data?.let {
                if (it.online) {
                    status.text = "已连接"
                } else {
                    status.text = "未连接"
                }
            }
        }
    }

    private fun login() {
        GlobalScope.async(Dispatchers.Main) {
            val viewContext = this@TjuNetViewComponent.context
            if (TjuNetPreferences.password == "") {
                Toasty.info(viewContext, "请前往上网功能页面登陆以保存您的密码").show()
                val intent = Intent(viewContext, TjuNetActivity::class.java)
                viewContext.startActivity(intent)
                return@async
            }
            val body = RealTjuNetService.login(
                    username = TjuNetPreferences.username,
                    password = TjuNetPreferences.password
            ).awaitAndHandle {
                Toasty.error(viewContext, "好像出了什么问题，${it.message}").show()
            }
            body?.let {
                if (body.data?.startsWith("login_ok") == true) {
                    status.text = "已连接"
                }
            }
        }
    }

    private fun logout() {
        GlobalScope.async(Dispatchers.Main) {
            val viewContext = this@TjuNetViewComponent.context
            val body = RealTjuNetService.logoutTry(
                    username = TjuNetPreferences.username,
                    password = TjuNetPreferences.password
            ).awaitAndHandle {
                Toasty.error(viewContext, "好像出了什么问题，${it.message}").show()
            }
            body?.let {
                status.text = it.data
            }
        }
    }

    fun bind() {
        refreshNetworkInfo()
        itemView.setOnClickListener {
            val intent = Intent(context,TjuNetActivity::class.java)
            context.startActivity(intent)

        }
        refreshBtn.setOnClickListener {
            status.text = "刷新中..."
            refreshNetworkInfo()
        }
        loginBtn.setOnClickListener {
            status.text = "连接中..."
            login()
            refreshNetworkInfo()
        }
        logoutBtn.setOnClickListener {
            status.text = "正在注销..."
            logout()
            refreshNetworkInfo()
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

    fun Button.setClickableState(clickable: Boolean) {
        if (clickable) {
            this.isClickable = true
            this.textColor = getColorCompat(R.color.colorAccent)
        } else {
            this.isClickable = false
            this.textColor = Color.GRAY
        }
    }

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup, owner: LifecycleOwner): TjuNetViewComponent {
            val view = inflater.inflate(R.layout.tjunet_item_network, parent, false)
            return TjuNetViewComponent(view, owner)
        }
    }

}