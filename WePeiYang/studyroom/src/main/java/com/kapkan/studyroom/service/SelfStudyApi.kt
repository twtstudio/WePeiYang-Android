package com.example.studyroom.service

import android.arch.lifecycle.MutableLiveData
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.CoroutineCallAdapterFactory
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val BASEURL: String = "https://selfstudy.twt.edu.cn"

interface SelfStudyApi {


    @GET("${BASEURL}/api/getBuildingList.php")
    fun getBuildingList(): Deferred<BuildingList>

    //获取当日可以上自习的教室(term暂时写死)
    @GET("${BASEURL}/api/getDayData.php?term=18191")
    fun getAvaliableRoom(@Part("week") week: Int,
                         @Part("day") day: Int): Deferred<AvailableRoomList>

    //获取某节课的可用的自习室(term暂时写死)
    @GET("${BASEURL}/api/getDayData.php?term=18191")
    fun getAvaliableRoombyClass(@Part("week") week: Int,
                                @Part("day") day: Int,
                                @Part("course") course: Int): Deferred<AvailableRoomList>

    //获得当前用户收藏列表，但是参数没列出来不知道写什么(雾
    //Header
    //Parameters
    //returns
    @GET("${BASEURL}/api/getCollectionList.php")
    fun getCollectionList()

    //收藏
    @POST("${BASEURL}/api/addCollection.php")
    fun starClassroom(@Part("classroom_ID") roomID: String): Deferred<Response>

    //取消收藏
    @POST("${BASEURL}/api/deleteCollection.php")
    fun unStarClassroom(@Part("classroom_ID") roomID: String): Deferred<Response>

    //获取某教室整周排版情况
    @Multipart
    @GET("${BASEURL}/api/getClassroomWeekInfo.php")
    fun getClassroomWeekInfo(@Part("classroom_ID") roomID: String,
                             @Part("week") week: Int,
                             @Part("term") term: Int = 18191): Deferred<ClassroomWeekInfo>

    //登陆？？？
    @GET("${BASEURL}/api/login.php")
    fun login()

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


