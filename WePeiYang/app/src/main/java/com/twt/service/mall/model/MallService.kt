package com.twt.service.mall.model

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.GET
import java.net.HttpCookie


interface MallApi {
    @GET("/api.php/Login/wpyLogin?model=1")
    fun login(@Field("token") token: String): Deferred<CommonBody<List<Login>>>

    @GET("api.php/User/myself_info")
    fun getPerInfo(@Field("cookies") cookie: HttpCookie): Deferred<CommonBody<List<PerInfo>>>

    @GET("api.php/Items/item_new")
    fun latestGoods(): Deferred<CommonBody<List<Goods>>>

    @GET("api.php/Items/search")
    fun schGoods(): Deferred<CommonBody<List<SchGoods>>>

    @GET("api.php/Items/menu")
    fun getMenu(): Deferred<CommonBody<List<Menu>>>


    companion object : MallApi by MallApiService()
}


object MallApiService {
    private const val baseUrl = "https://mall.twt.edu.cn"
    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
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

data class PerInfo(
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