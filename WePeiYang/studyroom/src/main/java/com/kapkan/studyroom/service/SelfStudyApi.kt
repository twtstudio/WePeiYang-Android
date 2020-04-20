package com.kapkan.studyroom.service

import android.arch.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.CoroutineCallAdapterFactory
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


const val BASEURL: String = "https://selfstudy.twt.edu.cn"
const val DATEURL = "https://open.twt.edu.cn/api"

interface SelfStudyApi {

    @GET("$DATEURL/v1/classtable")
    fun getClassTable(): Deferred<CommonBody<Classtable>>

    @GET("$BASEURL/api/getBuildingList.php")
    fun getBuildingList(): Deferred<BuildingList>

    //获取当日可以上自习的教室(term暂时写死)

    @GET("$BASEURL/api/getDayData.php?term=19202")
    fun getAvaliableRoom(@Query("week") week: Int,
                         @Query("day") day: Int): Deferred<AvailableRoomList>

    //获取某节课的可用的自习室(term暂时写死)

    @GET("$BASEURL/api/getDayData.php?term=19202")
    fun getAvaliableRoombyClass(@Query("week") week: Int,
                                @Query("day") day: Int,
                                @Query("course") course: Int): Deferred<AvailableRoomList>

    //获得当前用户收藏列表，但是参数没列出来不知道写什么(雾
    //Header
    //Parameters
    //returns
    @GET("$BASEURL/api/getCollectionList.php")
    fun getCollectionList(): Deferred<CollectionList>

    //收藏
    @Multipart
    @POST("$BASEURL/api/addCollection.php")
    fun starClassroom(@Part("token") token: String,
                      @Part("classroom_ID") roomID: String): Deferred<Response>

    //取消收藏
    @Multipart
    @POST("$BASEURL/api/deleteCollection.php")
    fun unStarClassroom(@Part("token") token: String,
                        @Part("classroom_ID") roomID: String): Deferred<Response>

    //获取某教室整周排版情况

    @GET("$BASEURL/api/getClassroomWeekInfo.php")
    fun getClassroomWeekInfo(@Query("classroom_ID") roomID: String,
                             @Query("week") week: Int,
                             @Query("term") term: Int = 19202): Deferred<ClassroomWeekInfo>

    //登陆？？？
    ///api.php/Login/wpyLogin?model=1
    ///api/login.php
    @GET("$BASEURL/api.php")
    fun login(@Header("Authorization") token: String): Deferred<CommonBody<Login>>

    companion object : SelfStudyApi by SelfStudyApiService()
}

object SelfStudyApiService {
    private val clientBuilder = OkHttpClient.Builder()

    private val client = clientBuilder
            .addInterceptor(HttpLoggingInterceptor())
            .build()

    val retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    inline operator fun <reified T> invoke(): T = retrofit.create(T::class.java)
}

val loginLiveData = MutableLiveData<Login>()
val BuildingListData = MutableLiveData<BuildingList>()
val AvailableRoomListData = MutableLiveData<AvailableRoomList>()
val collectionLiveData = MutableLiveData<CollectionList>()


interface TjuCourseApi {

    @GET("v1/classtable")
    fun getClassTable(): Deferred<CommonBody<Classtable>>

    companion object : TjuCourseApi by ServiceFactory()

}

fun getToken(): String {
    return "Bearer{${CommonPreferences.token}}"
}


data class BuildingList(
        val `data`: List<Data>,
        val error_code: Int,
        val message: String
)

data class Data(
        val area_id: Any,
        val building: String,
        val building_id: String,
        val campus_id: String,
        val classrooms: List<Classroom>
)

data class Classroom(
        val building_id: String,
        val capacity: String,
        val classroom: String,
        val classroom_id: String
)

data class AvailableRoomList(
        val `data`: List<Data>,
        val error_code: Int,
        val message: String
)

data class AvailableRoombyClass(
        val `data`: List<Data>,
        val error_code: Int,
        val message: String
)

data class CollectionList(
        val `data`: List<Data>,
        val error_code: Int,
        val message: String
)

data class Login(
        val token: String,
        val twt_id: Int,
        val uid: String
)

data class Response(
        val `data`: List<Any>,
        val error_code: Int,
        val message: String
)

data class ClassroomWeekInfo(
        val `data`: weekdata,
        val error_code: Int,
        val message: String
)

data class weekdata(
        val `1`: String,
        val `2`: String,
        val `3`: String,
        val `4`: String,
        val `5`: String,
        val `6`: String,
        val `7`: String
)

data class Classtable(val week: Int = 0,
                      val cache: Boolean = true,
                      @SerializedName("data") val courses: List<Course>,
                      @SerializedName("term_start") val termStart: Long = 0L,
                      @SerializedName("updated_at") val updatedAt: String = "",
                      val term: String = "")

data class Course(val coursetype: String = "",
                  val college: String = "",
                  val ext: String = "",
                  val classid: Int = 0, // 逻辑班号
                  val teacher: String = "",
                  val week: Week,
                  val coursename: String = "",
                  @SerializedName("arrange") val arrangeBackup: List<Arrange>,
                  val campus: String = "",
                  val coursenature: String = "",
                  val credit: String = "",
                  val courseid: String = "0", // 课程编号
                  var courseColor: Int = 0,
                  var statusMessage: String? = "", // [蹭课] [非本周] 什么的
                  var weekAvailable: Boolean = false, // 是不是灰色
                  var dayAvailable: Boolean = false) // 今天有没有课

data class Week(val start: Int = 0,
                val end: Int = 0)

data class Arrange(val week: String = "",/*单双周 单周 双周*/
                   val start: Int = 0,/*第几节开始*/
                   val end: Int = 0,/*第几节结束*/
                   val day: Int = 0,/*周几*/
                   val room: String = ""/*上课地点*/)
