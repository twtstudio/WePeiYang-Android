package com.twtstudio.retrox.auth.api

import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface AuthApi {
    @FormUrlEncoded
    @POST("/api/auth/common")
    fun login(@Field("account") studentNumber: String, @Field("password") password: String): Deferred<AuthBody<NewAuthSelfBean>>

    @GET("/api/user/single")
    fun newAuthSelf(): Deferred<AuthBody<NewAuthSelfBean>>

    @FormUrlEncoded
    @POST("/api/register/phone/msg")
    fun getCaptcha(@Field("phone") phoneNumber: String): Deferred<AuthBody<String>>

    @FormUrlEncoded
    @POST("/api/register")
    fun register(@Field("userNumber") userNumber: String,
                 @Field("nickname") nickname: String,
                 @Field("phone") phone: String,
                 @Field("verifyCode") verifyCode: String,
                 @Field("password") password: String,
                 @Field("email") email: String,
                 @Field("idNumber") idNumber: String): Deferred<AuthBody<String>>

    @FormUrlEncoded
    @PUT("/api/user/single")
    fun infoSupple(@Field("telephone") phone: String,
                 @Field("verifyCode") verifyCode: String,
                 @Field("email") email: String): Deferred<AuthBody<String>>

    companion object : AuthApi by AuthServiceFactory()

}


data class NewAuthSelfBean(
        val userNumber: String,
        val nickname: String,
        val telephone: String,
        val email: String,
        val token: String,
        val role: String,
        val realname: String,
        val gender: String,
        val department: String,
        val major:String,
        val stuType:String,
        val avatar: String
)

data class AuthBody<out T>(
        val error_code: Int,
        val message: String,
        val result: T?
)