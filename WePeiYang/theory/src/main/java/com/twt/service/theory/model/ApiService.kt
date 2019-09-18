package com.twt.service.theory.model

import com.google.gson.annotations.SerializedName
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
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
    fun getPaper(@Query("paper_id") paper_id: Int, @Header("Authorization") token: String = "Bearer{${CommonPreferences.token}}"): Deferred<PaperBean>

    @GET("$THEORY_BASE_URL/getTests")
    fun getTests(@Header("Authorization") token: String = "Bearer{${CommonPreferences.token}}"): Deferred<TestBean>

    @GET("$THEORY_BASE_URL/session")
    fun getSession(@Header("Authorization") token: String = "Bearer{${CommonPreferences.token}}"): Deferred<ResponseBody>

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

class PaperBean {
    /**
     * head : {"id":105,"name":"新未命名试卷","college_code":"210","test_time":"10","duration":"35","started_at":"2019-09-17 22:59:16","ended_time":"2035-10-01 23:59:00","is_exist":"1","created_at":"2019-09-18 00:07:31","updated_at":"2019-09-17 16:08:08","status":"已发布"}
     * body : [{"id":4922,"question":"2018年10月15日上午，中国商飞公司与天骄航空在北京签署ARJ21飞机购机协议，计划2018年底向天骄航空交付首批ARJ21飞机。这标志着首个国产喷气客机机队即将落户（ ），天骄航空将成为首家国产喷气客机机队运营平台。","objA":"青岛","objB":"酒泉","objC":"内蒙古","objD":"甘肃","type":"sc"},{"id":2749,"question":"国家主席习近平在二〇一九年新年贺词中指出，2018年，我们过得很充实、走得很坚定。成就是（ ）。","objA":"全国各族人民撸起袖子干出来的","objB":"新时代奋斗者挥洒汗水拼出来的","objC":"来之不易的","objD":"中华儿女努力奋斗出来的","objE":"","objF":"","type":"mc"}]
     * time : 2019-09-18 11:39:39
     */

    var head: HeadBean? = null
    var time: String? = null
    var body: List<BodyBean>? = null

    class HeadBean {
        /**
         * id : 105
         * name : 新未命名试卷
         * college_code : 210
         * test_time : 10
         * duration : 35
         * started_at : 2019-09-17 22:59:16
         * ended_time : 2035-10-01 23:59:00
         * is_exist : 1
         * created_at : 2019-09-18 00:07:31
         * updated_at : 2019-09-17 16:08:08
         * status : 已发布
         */

        var id: Int = 0
        var name: String? = null
        var college_code: String? = null
        var test_time: String? = null
        var duration: String? = null
        var started_at: String? = null
        var ended_time: String? = null
        var is_exist: String? = null
        var created_at: String? = null
        var updated_at: String? = null
        var status: String? = null
    }

    class BodyBean {
        /**
         * id : 4922
         * question : 2018年10月15日上午，中国商飞公司与天骄航空在北京签署ARJ21飞机购机协议，计划2018年底向天骄航空交付首批ARJ21飞机。这标志着首个国产喷气客机机队即将落户（ ），天骄航空将成为首家国产喷气客机机队运营平台。
         * objA : 青岛
         * objB : 酒泉
         * objC : 内蒙古
         * objD : 甘肃
         * type : sc
         * objE :
         * objF :
         */

        var id: Int = 0
        var question: String? = null
        var objA: String? = null
        var objB: String? = null
        var objC: String? = null
        var objD: String? = null
        var type: String? = null
        var objE: String? = null
        var objF: String? = null
    }
}

class SessionBean {
    /**
     * _token : QvJYCBD3EpKVNTrnLag1cv6viJAhVXjDtFDbHMJS
     * data : {"id":94481,"user_number":"3018210153","twt_name":"Twifor","avatar":"2018/09/13/1536801644-94481-3018210153.jpg","type":0,"token":"Authorization:Bearer{eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIyNzI1LCJpc3MiOiJodHRwczovL29wZW4udHd0c3R1ZGlvLmNvbS9hcGkvdjEvYXV0aC90b2tlbi9nZXQiLCJpYXQiOjE1Njg3NzA3MzAsImV4cCI6MTU2OTM3NTUzMCwibmJmIjoxNTY4NzcwNzMwLCJqdGkiOiJlQkR4MjRxQ0RnT1l0MEp6In0.QJMkZFj9GtAQFFPpRTsU_vxzGaKsd9E9NKdYhKjjrZs}","real_name":"李雨寒"}
     * _flash : {"old":[],"new":[]}
     */

    var _token: String? = null
    var data: DataBean? = null
    var _flash: FlashBean? = null

    class DataBean {
        /**
         * id : 123
         * user_number : 2333
         * twt_name : xxx
         * avatar : 2018/09/13/1536801644-94481-3018210153.jpg
         * type : 0
         * token : Authorization:Bearer{eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIyNzI1LCJpc3MiOiJodHRwczovL29wZW4udHd0c3R1ZGlvLmNvbS9hcGkvdjEvYXV0aC90b2tlbi9nZXQiLCJpYXQiOjE1Njg3NzA3MzAsImV4cCI6MTU2OTM3NTUzMCwibmJmIjoxNTY4NzcwNzMwLCJqdGkiOiJlQkR4MjRxQ0RnT1l0MEp6In0.QJMkZFj9GtAQFFPpRTsU_vxzGaKsd9E9NKdYhKjjrZs}
         * real_name : aaa
         */

        var id: Int = 0
        var user_number: String? = null
        var twt_name: String? = null
        var avatar: String? = null
        var type: Int = 0
        var token: String? = null
        var real_name: String? = null
    }

    class FlashBean {
        var old: List<*>? = null
        @SerializedName("new")
        var newX: List<*>? = null
    }
}
