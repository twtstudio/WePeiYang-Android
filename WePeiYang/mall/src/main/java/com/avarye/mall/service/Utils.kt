package com.avarye.mall.service

import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import okhttp3.MediaType
import okhttp3.RequestBody
import java.net.HttpCookie

object Utils {
    private val cookies = mutableListOf<String>()
    private val headerToken = "Bearer{${CommonPreferences.token}}"
    private var infoLogin: Login? = null
    private val infoGoods = mutableListOf<Goods>()
    private val infoSearch = mutableListOf<SchGoods>()

    fun getLogin(): Login? {
        return infoLogin
    }

    fun setLogin(data: Login?) {
        infoLogin = data
    }

    fun getGoods(): List<Goods> {
        return infoGoods
    }

    fun addGoods(data: List<Goods>) {
        infoGoods.clear()
        this.infoGoods += data
    }

    fun clearGoods() {
        this.infoGoods.clear()
    }

    fun getSchGoods(): List<SchGoods> {
        return infoSearch
    }

    fun addSchGoods(data: List<SchGoods>) {
        infoSearch += data
    }

    fun clearSchGoods() {
        this.infoSearch.clear()
    }

    //TODO:还没写
    fun getCampus(i: String?) = when (i) {
        "1" -> "卫津路"
        "2" -> "北洋园"
        else -> "未知"
    }

    fun getBargin(i: String) = when (i) {//I have no idea
        "0" -> ""
        "1" -> "可刀"
        "2" -> ""
        else -> ""
    }

    //token
    fun getToken(): String {
        return headerToken
    }

    //Cookies
    fun saveCookie(cookie: String) {
        cookies.add(cookie)
    }

    fun getCookie(): MutableList<String> {
        return cookies
    }

    //处理request body
    fun toReqBody(page: Int): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), page.toString())
    }

    fun toReqBody(key: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), key)
    }

}