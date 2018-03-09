package com.twt.wepeiyang.commons.experimental

import com.orhanobut.logger.Logger
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by rickygao on 2018/3/5.
 */

object ServiceFactory {

    const val SERVER_HOST = "open.twtstudio.com"
    private const val BASE_URL = "https://$SERVER_HOST/api/"

    const val APP_KEY = "9GTdynvrCm1EKKFfVmTC"
    const val APP_SECRET = "1aVhfAYBFUfqrdlcT621d9d6OzahMI"
//    const val FIR_API_TOKEN = "421d45c1fa7b7c4358667ffcedf4638e"

    private val loggingInterceptor = HttpLoggingInterceptor {
        if (it.startsWith('{') && it.length < 400) Logger.json(it)
        else Logger.d(it)
    }.apply { level = HttpLoggingInterceptor.Level.BODY }


    val client = OkHttpClient.Builder()
            .addInterceptor(UserAgentInterceptor)
            .addInterceptor(SignatureInterceptor)
            .addInterceptor(AuthorizationInterceptor)
            .authenticator(RealAuthenticator)
            .addNetworkInterceptor(loggingInterceptor)
            .addNetworkInterceptor(CodeCorrectionInterceptor)
            .build()

    val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    inline operator fun <reified T> invoke(): T = retrofit.create(T::class.java)

}

internal val Request.trusted: Boolean
    get() = url().host() == ServiceFactory.SERVER_HOST

data class CommonBody<out T>(
        val error_code: Int,
        val message: String,
        val data: T?
)