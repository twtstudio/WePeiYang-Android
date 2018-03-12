package com.twt.wepeiyang.commons.experimental.service

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by rickygao on 2018/3/5.
 */

interface AuthService {

    @GET("v1/auth/token/get")
    fun getToken(@Query("twtuname") twtuname: String, @Query("twtpasswd") twtpasswd: String): Deferred<CommonBody<Token>>

    @GET("v2/auth/self")
    fun authSelf(): Deferred<AuthSelfBean>

    @GET("v1/auth/token/refresh")
    fun refreshToken(): Deferred<CommonBody<Token>>

}

object RealAuthService : AuthService by ServiceFactory()

data class AuthSelfBean(
        val twtid: Int,
        val twtuname: String,
        val realname: String,
        val studentid: String,
        val avatar: String,
        val accounts: Accounts,
        val dropout: Int // 0: 未操作，1: 已退学，2: 已复学
)

data class Accounts(val tju: Boolean, val lib: Boolean)

data class Token(val token: String)