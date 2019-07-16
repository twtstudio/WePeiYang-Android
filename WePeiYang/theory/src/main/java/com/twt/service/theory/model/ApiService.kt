package com.twt.service.theory.model

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface TheoryApi{
    //single sign in
    @GET("v1/theory/exam/getLatestExam")
    fun getLatestExam(@Query("num") num : Int) : Deferred<CommonBody<String>>
    // check
    @GET("v1/theory/notice/getLatestNotcie")
    fun getLatestNotice(@Query("num") num : Int) : Deferred<CommonBody<String>>

   @GET("v1/theory/login/testLogin")
   fun testLogin() : Deferred<CommonBody<String>>

   @GET("v1/theory/login/loginStatus")
   fun loginStatus() : Deferred<CommonBody<String>>

   @GET("v1/theory/user/getUserExam")
   fun getUserExam(@Query("user_id") userid : Int) : Deferred<CommonBody<String>>



   companion object : TheoryApi by ServiceFactory()

}