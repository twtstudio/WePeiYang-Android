package com.avarye.mall.service

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.cache.Cache
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface MallApi {

    @GET("/api.php/Login/wpyLogin?model=1")
    fun loginAsync(@Header("Authorization") token: String): Deferred<CommonBody<Login>>

    @GET("api.php/User/myself_info")
    fun getMyInfoAsync(): Deferred<MyInfo>

    @Multipart
    @POST("api.php/User/myself_change")
    fun changeMyInfoAsync(@Part("phone") phone: RequestBody? = null,
                          @Part("email") email: RequestBody? = null,
                          @Part("qq") qq: RequestBody? = null,
                          @Part("xiaoqu") campus: RequestBody? = null): Deferred<Result>

    @Multipart
    @POST("api.php/User/campus_change")
    fun changeCampusAsync(@Part("xiaoqu") campus: RequestBody): Deferred<Result>

    @Multipart
    @POST("api.php/Items/item_new")
    fun latestSaleAsync(@Part("yeshu") page: RequestBody,
                        @Part("which") which: RequestBody): Deferred<List<Sale>>

    @Multipart
    @POST("api.php/Items/item_new")
    fun latestNeedAsync(@Part("yeshu") page: RequestBody,
                        @Part("which") which: RequestBody): Deferred<List<Sale>>

    @Multipart
    @POST("api.php/Items/search")
    fun searchAsync(@Part("key") key: RequestBody,
                    @Part("yeshu") page: RequestBody): Deferred<List<Sale>>

    @GET("api.php/Items/menu")
    fun getMenuAsync(): Deferred<List<Menu>>

    @Multipart
    @POST("api.php/Items/items_fenlei")
    fun selectAsync(@Part("category") category: RequestBody,
                    @Part("yeshu") page: RequestBody,
                    @Part("which") which: RequestBody): Deferred<List<Sale>>

    @GET("api.php/Items/item_one")
    fun getDetailAsync(@Query("id") id: String): Deferred<Sale>

    @Multipart
    @POST("api.php/Items/saler_info")
    fun getSellerInfoAsync(@Part("token") token: RequestBody,
                           @Part("gid") gid: RequestBody): Deferred<Seller>

    @GET("api.php/User/userinfo_for_app")
    fun getUserInfoAsync(@Query("id") id: String): Deferred<UserInfo>

    @Multipart
    @POST("api.php/Upload/img_upload")
    fun postImgAsync(@Part partList: List<MultipartBody.Part>): Deferred<Result>

    @Multipart
    @POST("api.php/Items/shoucang")
    fun favAsync(@Part("token") token: RequestBody,
                 @Part("gid") gid: RequestBody): Deferred<Result>

    @Multipart
    @POST("api.php/Items/shoucang_quxiao")
    fun deFavAsync(@Part("token") token: RequestBody,
                   @Part("gid") gid: RequestBody): Deferred<Result>

    @Multipart
    @POST("api.php/Items/shoucang_list")
    fun getFavListAsync(@Part("token") token: RequestBody): Deferred<List<Sale>>

    @Multipart
    @POST("api.php/Items/comment_list")
    fun getCommentListAsync(@Part("token") token: RequestBody,
                            @Part("which") which: RequestBody,
                            @Part("id") id: RequestBody): Deferred<List<CommentList>>

    @Multipart
    @POST("api.php/Items/comment_do")
    fun commentAsync(@Part("content") content: RequestBody,
                     @Part("token") token: RequestBody,
                     @Part("which") which: RequestBody,
                     @Part("tid") tid: RequestBody): Deferred<Result>

    @Multipart
    @POST("api.php/Items/huifu_list")
    fun getReplyListAsync(@Part("token") token: RequestBody,
                          @Part("cid") cid: RequestBody): Deferred<List<CommentList>>

    @Multipart
    @POST("api.php/Items/hui_do")
    fun replyAsync(@Part("token") token: RequestBody,
                   @Part("cid") cid: RequestBody,
                   @Part("content") content: RequestBody): Deferred<Result>

    @Multipart
    @POST("api.php/Items/sale_fabu")
    fun postSaleAsync(@Part partList: List<MultipartBody.Part>): Deferred<Result>

    @Multipart
    @POST("api.php/Items/item_off")
    fun deleteSaleAsync(@Part("token") token: RequestBody,
                        @Part("gid") cid: RequestBody): Deferred<Result>

    //TODO:待测试

    @POST("api.php/Items/need_fabu")
    fun postNeedAsync(@Part partList: List<MultipartBody.Part>): Deferred<Result>

    companion object : MallApi by MallApiService()
}

val loginLiveData = MutableLiveData<Login>()
val saleLiveData = MutableLiveData<List<Sale>>()
val needLiveData = MutableLiveData<List<Sale>>()
val searchLiveData = MutableLiveData<List<Sale>>()
val selectLiveData = MutableLiveData<List<Sale>>()
val myListLiveData = MutableLiveData<List<Sale>>()
val myFavLiveData = MutableLiveData<List<Sale>>()
val imgIdLiveData = MutableLiveData<String>()

private val menuLocalData = Cache.hawk<List<Menu>>("MALL_MENU")
private val menuRemoteData = Cache.from(MallApi.Companion::getMenuAsync)
val menuLiveData = RefreshableLiveData.use(menuLocalData, menuRemoteData)

private val mineLocalData = Cache.hawk<MyInfo>("MALL_MINE")
private val mineRemoteData = Cache.from(MallApi.Companion::getMyInfoAsync)
val mineLiveData = RefreshableLiveData.use(mineLocalData, mineRemoteData)


object MallApiService {
    private val cookie = object : CookieJar {
        val map = HashMap<String, MutableList<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
            map[url.host()] = cookies
            MallManager.saveCookie(cookies.toString())//测试用
            Log.d("login load cookie", MallManager.getCookie().toString())
        }

        override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
            Log.d("login load cookie", MallManager.getCookie().toString())
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

data class Result(
        val msg: String,
        val result_code: String,
        val img_url: String?,
        val id: String?,
        val cid: String?,
        val hid: String?
)

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
        val xiaoqu: String,
        val goods_count: Int,
        val needs_count: Int
)

data class Sale(
        val bargain: Any,
        val campus: String,
        val ctime: String,
        val gdesc: String,
        val id: String,
        val imgurl: String,
        val email: String,
        val icon: String,
        val label_name: Any,
        val location: String,
        val name: String,
        val page: Int,
        val phone: String,
        val price: String,
        val qq: String,
        val uid: String,
        val username: String,
        val exchange: String,
        val state: String
)

data class Menu(
        val icon: String,
        val id: String,
        val name: String,
        val smalllist: List<SmallList>
)

data class SmallList(
        val b_id: String,
        val id: String,
        val name: String
)

data class Seller(
        val email: String,
        val phone: String,
        val qq: String
)

data class UserInfo(
        val needs_list: List<Sale>?,
        val goods_list: List<Sale>?,
        val user_info: MyInfo
)

data class CommentList(
        val cid: String,
        val content: String,
        val ctime: String,
        val icon: String,
        val name: String,
        val uid: String
)


