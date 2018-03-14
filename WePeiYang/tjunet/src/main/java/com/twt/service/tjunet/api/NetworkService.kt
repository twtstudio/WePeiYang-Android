package com.twt.service.tjunet.api

import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import okhttp3.ResponseBody
import retrofit2.http.*
import kotlin.collections.HashMap

/**
 * Created by retrox on 2018/3/13.
 */
interface NetworkService {
    @FormUrlEncoded
    @POST("http://202.113.5.133/include/auth_action.php")
    fun logoutPost(@Field("username") username: String, @Field("password") password: String, @Field("action") action: String = "logout", @Field("ajax") ajax: Int = 1): Deferred<ResponseBody>

    @FormUrlEncoded
    @POST("http://202.113.5.133/include/auth_action.php")
    fun loginPost(@Field("username") username: String, @Field("password") password: String, @Field("action") action: String = "login", @FieldMap requestParam: RequestParam = RequestParam): Deferred<ResponseBody>

}

object RealNetWorkService: NetworkService by ServiceFactory()

object RequestParam: HashMap<String, String>() {
    init {
        put("ac_id", "13")
        put("nas_ip", "")
        put("user_mac", "")
        put("user_ip", "")
        put("save_me", "1")
        put("ajax", "1")
    }
}