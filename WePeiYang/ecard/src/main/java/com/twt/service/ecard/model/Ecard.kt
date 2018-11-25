package com.twt.service.ecard.model

import okhttp3.*
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * TjuECard 客户端请求工具
 * 工具 ecard.tju.edu.cn:10000 网络分析得到
 */

//CookieJar是用于保存Cookie的 拦截重定向时候的Cookie 用于认证
internal class LocalCookieJar : CookieJar {
    var cookies: List<Cookie>? = null
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return if (cookies != null && !url.toString().contains("login")) cookies!! else listOf() //登陆的时候不能给它cookie 否则不给新的cookie
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (url.toString().contains("login")) {
            this.cookies = cookies
            println(cookies)
        }
    }
}

private val ECARD_BASE_URL = "ecard.tju.edu.cn:10000"
private val client = OkHttpClient().newBuilder().followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
        .followSslRedirects(false)
        .connectTimeout(5, TimeUnit.SECONDS)
        .cookieJar(LocalCookieJar())
        .retryOnConnectionFailure(false)
        .build()

internal fun login(username: String, password: String) {

    val requestBody = FormBody.Builder()
            .add("_58_login_type", "_58_login_type")
            .add("_58_login", username)
            .add("_58_password", password)
            .build()

    val request = Request.Builder()
            .url("http://$ECARD_BASE_URL/c/portal/login")
            .header("Pragma", "no-cache")
            .addHeader("Cookie", "COOKIE_SUPPORT=true; GUEST_LANGUAGE_ID=zh_CN; LFR_SESSION_STATE_10926=1542787468192")
            .post(requestBody)
            .build()

    val response = client.newCall(request).execute()
    println(response.headers())
}

/**
 * 卡号：****
卡状态：正常
卡余额：57.87元
卡有效期：2020-08-10
未领取补助：0元
 */
data class ECardPersonInfo(val number: String, val status: String, val balance: String, val validityPeriod: String, val notReceivedMoney: String)

/**
 * 失败会抛出 @java.io.EOFException 此时需要重新登录 做个try catch
 */
internal fun fetchPersonInfo(): ECardPersonInfo {
    val request = Request.Builder()
            .url("http://$ECARD_BASE_URL/web/guest/personal")
            .build()
    val response = client.newCall(request).execute()
    val body = response.body()?.string() ?: ""
    val rootDocument = Jsoup.parse(body)
    val cardInfoDoc = rootDocument.getElementsByClass("card-info")
    val trs = cardInfoDoc.select("tbody").select("tr")
    var number = ""
    var status = ""
    var balance = ""
    var validityPeriod = ""
    var notReceivedMoney = ""
    trs.forEach {
        val nodes = it.select("td")[0]
        val text = nodes.text().split("：")
        if (text.size == 2) {
            when (text[0]) {
                "卡号" -> number = text[1]
                "卡状态" -> status = text[1]
                "卡余额" -> balance = text[1]
                "卡有效期" -> validityPeriod = text[1]
                "未领取补助" -> notReceivedMoney = text[1]
            }
        }
    }
    val info = ECardPersonInfo(number, status, balance, validityPeriod, notReceivedMoney)
    println(info)
    return info
}

data class TransactionInfo(val date: String, val time: String, val location: String, val amount: String, val balance: String)

/**
 * 获取交易记录 30天
 */
internal fun fetchHistory(): List<TransactionInfo> {

    val requestBody = FormBody.Builder()
            .add("_transDtl_WAR_ecardportlet_qdate", "30") // 查多少天 目前只能查40以内 否则会报错
            .add("_transDtl_WAR_ecardportlet_qtype", "2")
            .add("_transDtl_WAR_ecardportlet_cur", "1") // page num 分页加载
            .add("_transDtl_WAR_ecardportlet_delta", "1000") // 一页包含的量 给他一个1k 就不需要分页了
            .build()

    val request = Request.Builder()
            .url("http://$ECARD_BASE_URL/web/guest/personal?p_p_id=transDtl_WAR_ecardportlet&p_p_lifecycle=0&p_p_state=exclusive&p_p_mode=view&p_p_col_id=column-4&p_p_col_count=1&_transDtl_WAR_ecardportlet_action=dtlmoreview")
            .post(requestBody)
            .build()

    val response = client.newCall(request).execute()
    val body = response.body()?.string() ?: ""
    val rootDocument = Jsoup.parse(body)
    val trs = rootDocument.select(".trade_table").select("tbody").select("tr")
    if (trs[0].text().contains("交易日期")) {
        trs.removeAt(0)
    }
    val infoList = mutableListOf<TransactionInfo>()
    trs.forEach {
        val data = it.text().split(" ")
        if (data.size == 5) {
            infoList.add(TransactionInfo(date = data[0], time = data[1], location = data[2], amount = data[3], balance = data[4]))
        }
    }
    println(infoList)
    return infoList
}

fun List<TransactionInfo>.today(): List<TransactionInfo> {
    val dateFormat = SimpleDateFormat("yyyyMMdd")
    val today = dateFormat.format(Date().time)
    return this.toMutableList().filter { it.date == today }
}