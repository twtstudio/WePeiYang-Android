package com.twt.service.mall.model

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

val baseUrl = "https://mall.twt.edu.cn"

interface MallService {
    @GET("/api.php/Login/wpyLogin?model=1")
    fun login(): Deferred<CommonBody<List<Login>>>





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

data class ScGoods(
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