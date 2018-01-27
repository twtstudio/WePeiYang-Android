package com.twtstudio.retrox.auth.api

/**
 * Created by rickygao on 2018/1/27.
 */

data class Response<out T>(
        val error_code: Int,
        val message: String,
        val data: T?
)

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
