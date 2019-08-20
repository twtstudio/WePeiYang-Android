package com.avarye.mall.service

import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import kotlinx.coroutines.Deferred
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object MallManager {
    private val cookies = mutableListOf<String>()
    private lateinit var infoLogin: Login

    //manager
    fun login() = MallApi.loginAsync(getToken())

    fun getMyInfo() = MallApi.getMyInfoAsync()

    fun changeMyInfo(phone: String, email: String, qq: String, campus: Int) = MallApi.changeMyInfoAsync(
            phone = toReqBody(phone),
            email = toReqBody(email),
            qq = toReqBody(qq),
            campus = toReqBody(campus))

    fun changeCampus(campus: Int) = MallApi.changeCampusAsync(toReqBody(campus))

    fun latestSale(page: Int) = MallApi.latestSaleAsync(toReqBody(page), toReqBody(1))

    fun latestNeed(page: Int) = MallApi.latestNeedAsync(toReqBody(page), toReqBody(2))

    fun selectSale(category: String, page: Int) = MallApi.selectSaleAsync(
            which = toReqBody(1),
            page = toReqBody(page),
            category = toReqBody(category))

    fun selectNeed(category: String, page: Int) = MallApi.selectNeedAsync(
            which = toReqBody(2),
            page = toReqBody(page),
            category = toReqBody(category))

    fun search(key: String, page: Int) = MallApi.searchAsync(toReqBody(key), toReqBody(page))

    fun getMenu() = MallApi.getMenuAsync()

    fun getDetail(id: String) = MallApi.getDetailAsync(id)

    fun getSellerInfo(gid: String) = MallApi.getSellerInfoAsync(toReqBody(getLogin().token), toReqBody(gid))

    fun getUserInfo(id: String) = MallApi.getUserInfoAsync(id)

    fun postImg(file: File): Deferred<Result> {

        val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", getLogin().token)
                .addFormDataPart("file", file.name, imageBody)

        val list = builder.build().parts()
        return MallApi.postImgAsync(list)
    }

    fun fav(gid: String) = MallApi.favAsync(toReqBody(getLogin().token), toReqBody(gid))

    fun deFav(gid: String) = MallApi.deFavAsync(toReqBody(getLogin().token), toReqBody(gid))

    fun favorites() = MallApi.favoritesAsync(toReqBody(getLogin().token))

    fun getCommentList(id: String, which: Int) = MallApi.getCommentListAsync(
            token = toReqBody(getLogin().token),
            id = toReqBody(id),
            which = toReqBody(which))

    fun comment(id: String, which: Int, content: String) = MallApi.commentAsync(
            token = toReqBody(getLogin().token),
            content = toReqBody(content),
            which = toReqBody(which),
            tid = toReqBody(id))

    fun getReplyList(id: String) = MallApi.getReplyListAsync(toReqBody(getLogin().token), toReqBody(id))

    fun reply(id: String, content: String) = MallApi.replyAsync(
            token = toReqBody(getLogin().token),
            cid = toReqBody(id),
            content = toReqBody(content))

    fun postSale(map: Map<String, Any>): Deferred<Result> {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", getLogin().token)
                .addFormDataPart("name", map["name"].toString())
                .addFormDataPart("desc", map["detail"].toString())
                .addFormDataPart("campus", map["campus"].toString())
                .addFormDataPart("location", map["location"].toString())
                .addFormDataPart("price", map["price"].toString())
                .addFormDataPart("bargain", map["bargain"].toString())
                .addFormDataPart("category", map["category"].toString())
                .addFormDataPart("category_main", map["categoryMain"].toString())
                .addFormDataPart("status", map["status"].toString())
                .addFormDataPart("iid", map["iid"].toString())
                .addFormDataPart("exchange", map["exchange"].toString())
                .addFormDataPart("phone", map["phone"].toString())
                .addFormDataPart("email", map["email"].toString())
                .addFormDataPart("qq", map["qq"].toString())
        val list = builder.build().parts()
        return MallApi.postSaleAsync(list)
    }

    fun postNeed(map: Map<String, Any>): Deferred<Result> {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", getLogin().token)
                .addFormDataPart("name", map["name"].toString())
                .addFormDataPart("gdesc", map["detail"].toString())
                .addFormDataPart("campus", map["campus"].toString())
                .addFormDataPart("location", map["location"].toString())
                .addFormDataPart("price", map["price"].toString())
                .addFormDataPart("category", map["category"].toString())
                .addFormDataPart("category_main", map["categoryMain"].toString())
                .addFormDataPart("exchange", map["exchange"].toString())
                .addFormDataPart("phone", map["phone"].toString())
                .addFormDataPart("email", map["email"].toString())
                .addFormDataPart("qq", map["qq"].toString())
        val list = builder.build().parts()
        return MallApi.postNeedAsync(list)
    }

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

    fun getBargin(i: String) = when (i) {
        "0" -> "不可刀"
        "1" -> "可小刀"
        "2" -> "可刀"
        else -> "未知"
    }

    fun getStatus(i: String) = when (i) {
        "1" -> "旧"
        "5" -> "5成新"
        "6" -> "6成新"
        "7" -> "7成新"
        "8" -> "8成新"
        "9" -> "9成新"
        "99" -> "99新"
        "10" -> "全新"
        else -> ""
    }

    fun setStatus(i: String) = when (i) {
        "旧" -> 1
        "5成新" -> 5
        "6成新" -> 6
        "7成新" -> 7
        "8成新" -> 8
        "9成新" -> 9
        "99新" -> 99
        "全新" -> 10
        else -> ""
    }

    fun dealText(text: String): String {
        return if (text.length <= 3) {
            text
        } else {
            text.substring(0, 4) + "..."
        }
    }

    //token
    private fun getToken(): String {
        return "Bearer{${CommonPreferences.token}}"
    }

    //Cookies(测试用
    fun saveCookie(cookie: String) {
        cookies.add(cookie)
    }

    fun getCookie(): MutableList<String> {
        return cookies
    }

    //处理request body
    private fun toReqBody(int: Int): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), int.toString())
    }

    private fun toReqBody(key: String): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), key)
    }

    private fun toReqBody(file: File): MultipartBody.Part {
        val body: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        return MultipartBody.Part.createFormData("file", file.name, body)
    }
}