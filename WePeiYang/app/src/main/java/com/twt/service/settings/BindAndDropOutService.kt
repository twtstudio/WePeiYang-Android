package com.twt.service.settings

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Single

/**
 * Created by retrox on 2017/1/31.
 */

interface BindAndDropOutService {

    @GET("v1/auth/dropout")
    fun dropOut(@Query("mode") mode: Int): Single<CommonBody<String>>

    @GET("v1/auth/bind/tju")
    fun bindTju(@Query("tjuuname") tjuuname: String, @Query("tjupasswd") tjupasswd: String): Single<ResponseBody>

    @GET("v1/auth/unbind/tju")
    fun unbindTju(@Query("twtuname") twtuname: String): Single<ResponseBody>

}

object RealBindAndDropOutService : BindAndDropOutService by ServiceFactory()