package com.twt.service.push

import android.content.Context
import android.util.Log
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import me.ele.uetool.UETool
import okhttp3.FormBody
import okhttp3.Request
import retrofit2.HttpException
import java.io.InputStream
import java.util.*

/**
 * Created by retrox on 2018/3/19.
 *  default port: 10086
 *  use adb forward tcp:$yourLocalPort tcp:10086
 */
class DebugProxyServer : EmbedHttpServer(10086) {
    private val TAG = "Proxy♂Server"

    fun registerRoute(context: Context) {
        DebugProxyRouter.route {
            get("/ue/open") { queries, out ->
                Restarter.getForegroundActivity(context)?.apply {
                    runOnUiThread {
                        UETool.showUETMenu()
                    }
                    out.ok("Opened")
                }
            }
            get("/ue/close") { _, out ->
                UETool.dismissUETMenu()
                out.ok("closed")
            }
        }
    }

    override fun handle(method: String, path: String?, headers: HashMap<String, String>?, queries: MutableMap<String, String>?, input: InputStream, response: ResponseOutputStream) {
        super.handle(method, path, headers, queries, input, response)
        Log.e(TAG, "$method $path $headers $queries")
        val client = ServiceFactory.client
        val baseUrl = "https://open.twtstudio.com"
        var request: Request? = null
        when (method) {
            "GET" -> {
                if (!DebugProxyRouter.intercept(path ?: "", queries, response)) {
                    request = Request.Builder().url(baseUrl + path).get().build()
                } else {
                    Log.e(TAG, "Debug Route -> path: $path")
                }
            }
            "POST" -> {
                val builder = FormBody.Builder()
                val formString = String(input.readBytes())
                formString.split("&").forEach {
                    val pair = it.split("=")
                    val key = pair.getOrElse(0) { "" }
                    val value = pair.getOrElse(1) { "" }
                    builder.add(key, value)
                }
                val requestBody = builder.build()

                request = Request.Builder().url(baseUrl + path).post(requestBody).build()
            }
            "DELETE" -> {
                val formString = String(input.readBytes())
                request = Request.Builder().url(baseUrl + path).delete().build()
            }
            else -> {
                request = Request.Builder().url(baseUrl + path).build()
            }
        }

        if (request == null) return
        try {
            Log.e(TAG, "$request")
            val callResponse = client.newCall(request).execute()
            response.setStatusCode(callResponse.code())
            response.write(callResponse.body()?.bytes())
        } catch (e: HttpException) {
            Log.e(TAG, e.message())
            response.setStatusCode(500)
            response.write(e.response().errorBody()?.bytes())
        }

    }

    fun ResponseOutputStream.ok(text: String) {
        setStatusCode(200)
        write(text.toByteArray())
    }
}