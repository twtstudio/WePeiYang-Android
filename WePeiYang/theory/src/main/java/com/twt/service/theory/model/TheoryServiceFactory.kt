package com.twt.service.theory.model

import com.twt.wepeiyang.commons.experimental.network.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object TheoryServiceFactory {
    private val clientBuilder = OkHttpClient.Builder()
    private val client: OkHttpClient = clientBuilder
            .retryOnConnectionFailure(false)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()


    private const val baseUrl = "https://theory-new.twtstudio.com/index.php/api/"
    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    inline operator fun <reified T> invoke(): T = retrofit.create(T::class.java)
}