package com.twt.service.theory.model

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import kotlinx.coroutines.experimental.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import rx.Observable

interface TheoryApi {
//    //single sign in
//    @GET("v1/theory/exam/getLatestExam")
//    fun getLatestExam(@Query("num") num: Int): Deferred<CommonBody<String>>
//
//    // check
//    @GET("v1/theory/notice/getLatestNotcie")
//    fun getLatestNotice(@Query("num") num: Int): Deferred<CommonBody<String>>
//
//    @GET("v1/theory/login/testLogin")
//    fun testLogin(): Deferred<CommonBody<String>>
//
//    @GET("v1/theory/login/loginStatus")
//    fun loginStatus(): Deferred<CommonBody<String>>
//
//    @GET("v1/theory/user/getUserExam")
//    fun getUserExam(@Query("user_id") userid: Int): Deferred<CommonBody<String>>

    @GET("/random?")
    fun getPaper(@Query("paper_id") paper_id: Int): Deferred<CommonBody<String>>

    @GET("/getTests")
    fun getTests(@Header("Authorization") token: String): Deferred<ResponseBody>
//
//    @GET("loginStatus")
//    fun getLoginStatus(): Deferred<TheoryLoginStatusBean>
//
//    @GET("login")
//    fun login(@Header("Authorization") token: String):Deferred<Unit>

    companion object : TheoryApi by TheoryServiceFactory()
}
//
//data class TheoryLoginStatusBean(
//        val status: Int,
//        val id: Int?,
//        val user_number: String?,
//        val twt_name: String?,
//        val real_name: String?,
//        val college: String?,
//        val college_code: String?
//)
//
//data class TheoryTestsDataBean(
//        val data: List<TheoryTestDetailDataBean>?
//)
//
//data class TheoryTestDetailDataBean(
//        val college_code: String?,
//        val created_at: String?,
//        val duration: String?,
//        val ended_time: String?,
//        val id: Int?,
//        val is_exist: Boolean?,
//        val name: String?,
//        val score: String?,
//        val started_at: String?,
//        val status: String?,
//        val stu_status: String?,
//        val test_time: String?,
//        val tested_time: String?,
//        val updated_at: String?
//)