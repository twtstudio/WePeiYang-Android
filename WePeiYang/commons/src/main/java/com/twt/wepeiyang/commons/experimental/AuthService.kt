package com.twt.wepeiyang.commons.experimental

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by rickygao on 2018/3/5.
 */

interface AuthService {

    @GET("v1/auth/token/get")
    fun getToken(@Query("twtuname") twtuname: String, @Query("twtpasswd") twtpasswd: String): Call<CommonBody<Token>>

    @GET("v2/auth/self")
    fun authSelf(): Call<AuthSelfBean>

    @GET("v1/auth/token/refresh")
    fun refreshToken(): Call<CommonBody<Token>>

    @GET("v1/auth/dropout")
    fun dropOut(@Query("mode") mode: Int): Call<CommonBody<String>>

}

object RealAuthService : AuthService by ServiceFactory()

data class AuthSelfBean(
        val twtid: Int,
        val twtuname: String,
        val realname: String,
        val studentid: String,
        val avatar: String,
        val accounts: AccountsBean,
        val dropout: Int // 0: 未操作，1: 已退学，2: 已复学
)

data class AccountsBean(val tju: Boolean, val lib: Boolean)

data class Token(val token: String)