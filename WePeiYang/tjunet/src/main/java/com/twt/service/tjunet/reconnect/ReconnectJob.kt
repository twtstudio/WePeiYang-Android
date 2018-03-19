package com.twt.service.tjunet.reconnect

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.twt.service.tjunet.api.RealTjuNetService
import com.twt.service.tjunet.ext.fineSSID
import com.twt.service.tjunet.pref.TjuNetPreferences
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
/**
 * Created by retrox on 2018/3/19.
 */
class ReconnectJob : JobService() {

    companion object {
        const val jobId = 0
        fun getScheduler(context: Context): JobInfo {
            val builder = JobInfo.Builder(jobId, ComponentName(context.packageName, ReconnectJob::class.java.name))
                    .setPersisted(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setMinimumLatency(1000*5)
                    .setOverrideDeadline(1000*60)
//                    .setOverrideDeadline(1000 * 5)
            return builder.build()

        }
    }

    private val TAG = "TJUReconnectJob"

    override fun onStopJob(params: JobParameters?): Boolean {
        if (!TjuNetPreferences.autoConnect) {
            return false
        }
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        val context = this.applicationContext
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        if (!TjuNetPreferences.autoConnect) {
            return false
        }
        val networkInfo = connectivityManager?.activeNetworkInfo ?: return false
        when (networkInfo.type) {
            ConnectivityManager.TYPE_MOBILE -> {
                Log.i(TAG, "数据网络")
                return false
            }
            ConnectivityManager.TYPE_WIFI -> {
                wifiManager?.connectionInfo?.let {
                    val ssid = it.ssid.fineSSID
                    if (ssid == "tjuwlan" || ssid == "tjuwlan-lib") {
                        checkAndLogin(params = params, mContext = context)

                    } else return false
                }
            }
        }

        return true //告知系统耗时操作
    }

    private fun checkAndLogin(params: JobParameters?, mContext: Context) {
        Log.i(TAG, "检查TJU登录状态")
        async(UI) {
            val result = RealTjuNetService.getStatus().awaitAndHandle {
                Log.e(TAG, "TJU自动连接检查错误${it.message}")
            }
            if (result?.data?.online == false) {
                val body = RealTjuNetService.login(
                        username = TjuNetPreferences.username,
                        password = TjuNetPreferences.password
                ).awaitAndHandle {
                    Log.e(TAG, "TJU自动登陆失败，${it.message}")
                }
                if (body?.data?.startsWith("login_ok") == true) {
                    Toasty.success(mContext, "微北洋已为你自动登陆tju").show()
                }
            }
            jobFinished(params, true)
        }

    }
}