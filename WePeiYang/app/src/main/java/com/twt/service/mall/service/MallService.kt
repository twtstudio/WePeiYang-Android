package com.twt.service.mall.service

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.CoroutineCallAdapterFactory
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import kotlinx.coroutines.experimental.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.net.HttpCookie


interface MallApi {


    @GET("/api.php/Login/wpyLogin?model=1")
    fun login(@Header("Authorization") token: String): Deferred<CommonBody<List<Login>>>

    @GET("api.php/User/myself_info")
    fun getMyInfo(/*@Field("cookies") cookie: HttpCookie*/): Deferred<List<MyInfo>>


    @GET("api.php/Items/item_new")
    fun latestGoods(): Deferred<List<Goods>>

    @POST("api.php/Items/search")
    fun schGoods(@Field("key") key: String, @Field("yeshu") page: Int): Deferred<List<SchGoods>>

    @GET("api.php/Items/menu")
    fun getMenu(): Deferred<List<Menu>>

    @POST("api.php/Items/sale_fabu")
    fun upLoadSale(/*data class*/): Deferred<Any>//TODO:么的数据


    companion object : MallApi by MallApiService()
}


object MallApiService {

    private val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(ReceivedCookiesInterceptor())
    private val client: OkHttpClient = clientBuilder.build()


    private const val baseUrl = "https://mall.twt.edu.cn"
    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    inline operator fun <reified T> invoke(): T = retrofit.create(T::class.java)
}

data class Login(
        val token: String,
        val twt_id: Int,
        val uid: String
)

data class MyInfo(
        val avatar: String,
        val email: String,
        val icon: String,
        val id: String,
        val level: String,
        val nicheng: String,
        val numb: String,
        val phone: String,
        val qq: String,
        val token: String,
        val xiaoqu: String
)

data class Goods(
        val bargain: Any,
        val campus: String,
        val ctime: String,
        val email: String,
        val gdesc: String,
        val icon: String,
        val id: String,
        val label_name: Any,
        val location: String,
        val name: String,
        val page: Int,
        val phone: String,
        val price: String,
        val qq: String,
        val uid: String,
        val username: String
)

data class SchGoods(
        val bargain: String,
        val campus: String,
        val ctime: String,
        val gdesc: String,
        val icon: String,
        val id: String,
        val imgurl: String,
        val label_name: String,
        val location: String,
        val name: String,
        val page: Int,
        val price: String,
        val username: String
)

data class Menu(
        val icon: String,
        val id: String,
        val name: String,
        val smalllist: List<Smalllist>
)

data class Smalllist(
        val b_id: String,
        val id: String,
        val name: String
)