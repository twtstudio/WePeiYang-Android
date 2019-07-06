package com.twt.service.mall.service

import java.net.HttpCookie

object Utils {//TODO:接口么有写 全靠猜
    fun getCampus(i:String) = when(i) {//I have no idea
        "1" -> "北洋园"
        "2" -> "卫津路"
        else -> ""
    }

    fun getBargin(i: String) = when(i) {//I have no idea
        "0" -> ""
        "1" -> "可刀"
        "2" -> ""
        else -> ""
    }

    //保存Cookies
    fun saveCookie(cookie: HttpCookie){

    }

    fun getCookie(): String {
        return ""
    }

}