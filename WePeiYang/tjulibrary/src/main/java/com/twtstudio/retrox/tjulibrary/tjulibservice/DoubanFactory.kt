package com.twtstudio.retrox.tjulibrary.tjulibservice

import com.twt.wepeiyang.commons.experimental.network.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object DoubanFactory {
    val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://api.douban.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    inline operator fun <reified T> invoke(): T = retrofit.create(T::class.java)
}