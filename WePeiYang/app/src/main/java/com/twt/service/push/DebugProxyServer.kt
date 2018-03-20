package com.twt.service.push

import android.util.Log
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.HashMap

/**
 * Created by retrox on 2018/3/19.
 *  default port: 10086
 *  use adb forward tcp:$yourLocalPort tcp:10086
 */
class DebugProxyServer : EmbedHttpServer(10086) {
    private val TAG = "Proxy♂Server"
    override fun handle(method: String, path: String?, headers: HashMap<String, String>?, queries: MutableMap<String, String>?, input: InputStream, response: ResponseOutputStream) {
        super.handle(method, path, headers, queries, input, response)
        Log.e(TAG, "$method $path $headers $queries")
        val client = ServiceFactory.client
        val baseUrl = "https://open.twtstudio.com"
        val request: Request
        when (method) {
            "GET" -> {
                request = Request.Builder().url(baseUrl + path).get().build()
            }
            "POST" -> {
                val mediaType = MediaType.parse("application/x-www-form-urlencoded")
                val requestBody = RequestBody.create(mediaType, input.readBytes())
                request = Request.Builder().url(baseUrl + path).post(requestBody).build()
            }
            "DELETE" -> {
                request = Request.Builder().url(baseUrl + path).delete().build()
            }
            else -> {
                request = Request.Builder().url(baseUrl + path).build()
            }
        }

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
}