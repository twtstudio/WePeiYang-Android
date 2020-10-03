package com.twt.service.announcement.service

import com.twt.wepeiyang.commons.experimental.network.CoroutineCallAdapterFactory
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AnnoServiceFactory {
    private val loggingInterceptor = HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }

    const val BASE_URL = "https://areas.twt.edu.cn/api/user/"

    val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(false)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

    val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    inline operator fun <reified T> invoke(): T = AnnoServiceFactory.retrofit.create(T::class.java)

}

data class CommonBody<out T>(
        val ErrorCode: Int,
        val msg: String,
        val data: T?
)