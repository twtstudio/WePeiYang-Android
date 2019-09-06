package com.twt.service.theory.model

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import rx.Observable

const val THEORY_BASE_URL = "https://theory-new.twtstudio.com/index.php/api"

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

    @GET("$THEORY_BASE_URL/random?")
    fun getPaper(@Query("paper_id") paper_id: Int): Deferred<CommonBody<String>>

    @GET("$THEORY_BASE_URL/getTests")
    fun getTests(): Deferred<TestBean>

    @GET("$THEORY_BASE_URL/session")
    fun getSession(): Deferred<ResponseBody>

//
//    @GET("loginStatus")
//    fun getLoginStatus(): Deferred<TheoryLoginStatusBean>
//
//    @GET("login")
//    fun login(@Header("Authorization") token: String):Deferred<Unit>

    companion object : TheoryApi by ServiceFactory()
}

class TestBean {

    var data: List<DataBean>? = null

    class DataBean {
        /**
         * college_code : 216
         * created_at : 2019-08-30 09:26:32
         * duration : 35
         * ended_time : 2035-10-01 23:59:00
         * id : 67
         * is_exist : 1
         * name : 234
         * score : 2
         * started_at : 2019-08-30 16:19:37
         * status : 已发布
         * stu_status : 未通过
         * test_time : 10
         * tested_time : 1
         * updated_at : 2019-08-30 09:27:24
         */

        var college_code: String? = null
        var created_at: String? = null
        var duration: String? = null
        var ended_time: String? = null
        var id: Int = 0
        var is_exist: String? = null
        var name: String? = null
        var score: String? = null
        var started_at: String? = null
        var status: String? = null
        var stu_status: String? = null
        var test_time: String? = null
        var tested_time: String? = null
        var updated_at: String? = null
    }
}