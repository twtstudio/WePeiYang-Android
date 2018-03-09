package com.twt.service.settings

import com.twt.wepeiyang.commons.experimental.CommonBody
import com.twt.wepeiyang.commons.experimental.ServiceFactory
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Single

/**
 * Created by retrox on 2017/1/31.
 */

interface AuthApi {

    @GET("v1/auth/token/get")
    fun login(@Query("twtuname") twtuname: String, @Query("twtpasswd") twtpasswd: String): Single<CommonBody<Token>>

    @GET("v2/auth/self")
    fun authSelf(): Single<AuthSelfBean>

    @GET("v1/auth/token/refresh")
    fun refreshToken(): Single<CommonBody<Token>>

    @GET("v1/auth/dropout")
    fun dropOut(@Query("mode") mode: Int): Single<CommonBody<String>>

    @GET("v1/auth/bind/tju")
    fun bindTju(@Query("tjuuname") tjuuname: String, @Query("tjupasswd") tjupasswd: String): Single<ResponseBody>

    @GET("v1/auth/unbind/tju")
    fun unbindTju(@Query("twtuname") twtuname: String): Single<ResponseBody>

}

object RealAuthApi : AuthApi by ServiceFactory()

data class AuthSelfBean(
        val twtid: Int,
        val twtuname: String,
        val realname: String,
        val studentid: String,
        val avatar: String,
        val accounts: AccountsBean,
        val dropout: Int //0: 未操作，1: 已退学，2: 已复学
)

data class AccountsBean(val tju: Boolean, val lib: Boolean)

data class Token(val token: String)
