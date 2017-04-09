package com.twtstudio.retrox.news.api

import com.twt.wepeiyang.commons.network.DefaultRetrofitBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by retrox on 09/04/2017.
 */

class PicProvider{

    private val retrofit:Retrofit = DefaultRetrofitBuilder.getBuilder()
            .baseUrl("https://photograph.twtstudio.com/")
            .build()
    val picApi:PicApi

    init {
        picApi = retrofit.create(PicApi::class.java)
    }

}
