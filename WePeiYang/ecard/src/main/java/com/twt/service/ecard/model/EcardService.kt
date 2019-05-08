package com.twt.service.ecard.model

import android.arch.lifecycle.MutableLiveData
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tjwhm@TWTStudio at 1:48 AM, 2018/11/29.
 * Happy coding!
 */


interface EcardService {
    @GET("v1/ecard/profile")
    fun getEcardProfile(@Query("cardnum") cardnum: String = EcardPref.ecardUserName, @Query("password") password: String = EcardPref.ecardPassword): Deferred<CommonBody<EcardProfileBean>>

    /**
     * @day: 查多少天的数据 超出饭卡补办期会导致异常
     */
    @GET("v1/ecard/turnover")
    fun getEcardTransaction(@Query("cardnum") cardnum: String = EcardPref.ecardUserName, @Query("password") password: String = EcardPref.ecardPassword, @Query("term") term: Int = 2): Deferred<CommonBody<List<TransactionInfo>>>

    @GET("v1/ecard/total")
    fun getEcardTotalConsumption(@Query("cardnum") cardnum: String = EcardPref.ecardUserName, @Query("password") password: String = EcardPref.ecardPassword): Deferred<CommonBody<EcardTotalConsumptionBean>>

    @GET("v1/ecard/QA")
    fun getFQA(): Deferred<CommonBody<List<ProblemBean>>>

    /**
     * @term: 天数。今日流水就传term=1，查n天内的就是传term=n
     */
    @GET("v1/ecard/pieChart")
    fun getEcardProportionalBarData(@Query("cardnum") cardnum: String = EcardPref.ecardUserName, @Query("password") password: String = EcardPref.ecardPassword, @Query("term") term: Int = 1): Deferred<CommonBody<List<EcardProportionalBarDataBean>>>

    // 一共取180天
    @GET("v1/ecard/lineChart")
    fun getEcardLineChartData(@Query("cardnum") cardnum: String = EcardPref.ecardUserName, @Query("password") password: String = EcardPref.ecardPassword): Deferred<CommonBody<List<EcardLineChartDataBean>>>

    companion object : EcardService by ServiceFactory()
}

// 个人详情
data class EcardProfileBean(
        val name: String,
        val balance: String, // 余额
        val cardnum: String,
        val cardstatus: String,
        val expiry: String, // 学生卡到期时间
        val subsidy: String, // 补助
        val amount: String
)

// 每日 + 每月 总消费
data class EcardTotalConsumptionBean(
        val total_day: Double,
        val total_month: Double,
        val total_30_days: Double
)

/**
 * type = 1 是充值, 2 是消费
 *
 * sub_type = "食堂" , "其它", "超市", "充值"
 */
data class TransactionInfo(
        val amount: String,
        val balance: String,
        val date: String,
        val location: String,
        val sub_type: String,
        val time: String,
        val type: Int
)

data class ProblemBean(
        val content: String,
        val date: String,
        val id: Int,
        val title: String
)

data class EcardProportionalBarDataBean(
        val total: Double,
        val type: String
)

data class EcardLineChartDataBean(
        val count: String,
        val date: String
)

var isBindECardBoolean = EcardPref.ecardUserName != "*" && EcardPref.ecardUserName != "" && EcardPref.ecardPassword != "*" && EcardPref.ecardPassword != ""

val isBindECardLiveData = MutableLiveData<Boolean>().apply {
    value = isBindECardBoolean
}

