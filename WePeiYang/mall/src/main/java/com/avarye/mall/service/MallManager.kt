package com.avarye.mall.service

import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import okhttp3.MediaType
import okhttp3.RequestBody

object MallManager {
    private val cookies = mutableListOf<String>()
    private val headerToken = "Bearer{${CommonPreferences.token}}"
    lateinit var infoLogin: Login
    private val infoGoods = mutableListOf<LatestSale>()
    private val infoNeed = mutableListOf<LatestNeed>()


    //manager
    fun login() = MallApi.loginAsync(getToken())

    fun getMyInfo() = MallApi.getMyInfoAsync()

    fun latestSale(page: Int) = MallApi.latestSaleAsync(toReqBody(page), toReqBody(1))

    fun latestNeed(page: Int) = MallApi.latestNeedAsync(toReqBody(page), toReqBody(2))

    fun search(key: String, page: Int) = MallApi.searchAsync(toReqBody(key), toReqBody(page))

    fun getMenu() = MallApi.getMenuAsync()

    fun getDetail(id: String) = MallApi.getDetailAsync(id)

    fun getSellerInfo(gid: String) = MallApi.getSellerInfoAsync(toReqBody(getLogin().token), toReqBody(gid))


    //data Utils
    private fun getLogin(): Login {
        return infoLogin
    }

    fun setLogin(data: Login) {
        infoLogin = data
    }

    fun getGoods(): List<LatestSale> {
        return infoGoods
    }

    fun addGoods(data: List<LatestSale>) {
        infoGoods.clear()
        this.infoGoods += data
    }

    fun clearGoods() {
        this.infoGoods.clear()
    }

    fun getNeed(): List<LatestNeed> {
        return infoNeed
    }

    fun addNeed(data: List<LatestNeed>) {
        infoNeed += data
    }

    fun clearNeed() {
        this.infoNeed.clear()
    }


    //数据处理
    fun getCampus(i: String?) = when (i) {
        "1" -> "卫津路"
        "2" -> "北洋园"
        else -> "未知"
    }

    fun getBargin(i: String) = when (i) {//I have no idea
        "0" -> "不可刀"
        "1" -> "可小刀"
        "2" -> "可刀"
        else -> "未知"
    }

    fun getState(i: String) = when (i) {
        "1" -> "旧"
        "5" -> "5成新"
        "6" -> "6成新"
        "7" -> "7成新"
        "8" -> "8成新"
        "9" -> "9成新"
        "99" -> "99新"
        "10" -> "全新"
        else -> "未知"
    }

    fun dealText(text: String): String {
        return if (text.length <= 2) {
            text
        } else {
            text.substring(0, 3) + "..."
        }
    }

    fun ifExchange(text: String): String {
        return if (text.isBlank()) {
            "否"
        } else {
            text
        }
    }

    //token
    fun getToken(): String {
        return "Bearer{${CommonPreferences.token}}"
    }

    //Cookies
    fun saveCookie(cookie: String) {
        cookies.add(cookie)
    }

    fun getCookie(): MutableList<String> {
        return cookies
    }

    //处理request body
    fun toReqBody(int: Int): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), int.toString())
    }

    fun toReqBody(key: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), key)
    }

}