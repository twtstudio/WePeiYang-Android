package com.avarye.mall.service

import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

object MallManager {
    private val cookies = mutableListOf<String>()
    private val headerToken = "Bearer{${CommonPreferences.token}}"
    private lateinit var infoLogin: Login

    //manager
    fun login() = MallApi.loginAsync(getToken())

    fun getMyInfo() = MallApi.getMyInfoAsync()

    fun changeMyInfo(phone: String, email: String, qq: String, campus: Int) =
            MallApi.changeMyInfoAsync(phone = toReqBody(phone), email = toReqBody(email),
                    qq = toReqBody(qq), campus = toReqBody(campus))

    fun changeCampus(campus: Int) = MallApi.changeCampusAsync(toReqBody(campus))

    fun latestSale(page: Int) = MallApi.latestSaleAsync(toReqBody(page), toReqBody(1))

    fun latestNeed(page: Int) = MallApi.latestNeedAsync(toReqBody(page), toReqBody(2))

    fun selectSale(category: String, page: Int) = MallApi.selectSaleAsync(which = toReqBody(1),
            page = toReqBody(page), category = toReqBody(category))

    fun selectNeed(category: String, page: Int) = MallApi.selectNeedAsync(which = toReqBody(2),
            page = toReqBody(page), category = toReqBody(category))

    fun search(key: String, page: Int) = MallApi.searchAsync(toReqBody(key), toReqBody(page))

    fun getMenu() = MallApi.getMenuAsync()

    fun getDetail(id: String) = MallApi.getDetailAsync(id)

    fun getSellerInfo(gid: String) = MallApi.getSellerInfoAsync(toReqBody(getLogin().token), toReqBody(gid))

    fun getUserInfo(id: String) = MallApi.getUserInfoAsync(id)

    fun uploadImg(file: File) = MallApi.uploadImgAsync(toReqBody(getLogin().token), toReqBody(file))

    fun fav(gid: String) = MallApi.favAsync(toReqBody(getLogin().token), toReqBody(gid))

    fun deFav(gid: String) = MallApi.deFavAsync(toReqBody(getLogin().token), toReqBody(gid))

    fun favorites() = MallApi.favoritesAsync(toReqBody(getLogin().token))

    fun getCommentList(id: String, which: Int) =
            MallApi.getCommentListAsync(token = toReqBody(getLogin().token), id = toReqBody(id), which = toReqBody(which))

    fun comment(id: String, which: Int, content: String) = MallApi.commentAsync(token = toReqBody(getLogin().token),
            content = toReqBody(content), which = toReqBody(which), tid = toReqBody(id))

    fun getReplyList(id: String) = MallApi.getReplyListAsync(token = toReqBody(getLogin().token), cid = toReqBody(id))

    fun reply(id: String, content: String) = MallApi.replyAsync(token = toReqBody(getLogin().token),
            cid = toReqBody(id), content = toReqBody(content))

    //data Utils
    private fun getLogin(): Login {
        return infoLogin
    }

    fun setLogin(data: Login) {
        infoLogin = data
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
    private fun toReqBody(int: Int): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), int.toString())
    }

    private fun toReqBody(key: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), key)
    }

    private fun toReqBody(file: File): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), file)
    }
}