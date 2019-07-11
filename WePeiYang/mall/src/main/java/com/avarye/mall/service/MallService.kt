package com.avarye.mall.service

import android.util.Log
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.net.HttpCookie
import kotlin.io.use


interface MallApi {

    @GET("/api.php/Login/wpyLogin?model=1")
    fun login(@Header("Authorization") token: String): Deferred<CommonBody<Login>>

    @GET("api.php/User/myself_info")
    fun getMyInfo(): Deferred<MyInfo>

    @Multipart
    @POST("api.php/Items/item_new")
    fun latestGoods(@Part("yeshu") page: RequestBody): Deferred<List<Goods>>

    @Multipart
    @POST("api.php/Items/search")
    fun schGoods(@Part("key") key: RequestBody, @Part("yeshu") page: RequestBody): Deferred<List<SchGoods>>

    @GET("api.php/Items/menu")
    fun getMenu(): Deferred<List<Menu>>

    //TODO:还没写
    @POST("api.php/Items/sale_fabu")
    fun upLoadSale(/*data class*/): Deferred<Any>

    @POST("api.php/Items/need_fabu")
    fun uploadNeed(/*data class*/): Deferred<Any>

    companion object : MallApi by MallApiService()
}

object MallApiService {
    private val cookie = object : CookieJar {
        val map = HashMap<String, MutableList<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
            map[url.host()] = cookies
            Utils.saveCookie(cookies.toString())
            Log.d("login load cookie", Utils.getCookie().toString())
        }

        override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
            Log.d("login load cookie", Utils.getCookie().toString())
            return map[url.host()] ?: ArrayList()
        }
    }


    private val clientBuilder = OkHttpClient.Builder()
            .cookieJar(cookie)
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
        var img: String,
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