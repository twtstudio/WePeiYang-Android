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
    fun getEcardTransaction(@Query("cardnum") cardnum: String = EcardPref.ecardUserName, @Query("password") password: String = EcardPref.ecardPassword, @Query("term") term: Int = 2): Deferred<CommonBody<TransactionListWrapper>>

    @GET("v1/ecard/total")
    fun getEcardTotalConsumption(@Query("cardnum") cardnum: String = EcardPref.ecardUserName, @Query("password") password: String = EcardPref.ecardPassword): Deferred<CommonBody<EcardTotalConsumptionBean>>

    @GET("v1/ecard/QA")
    fun getFQA(): Deferred<List<ProblemBean>>

    @GET("v1/ecard/dynamic")
    fun getDynamic(): Deferred<List<ProblemBean>>

    /**
     * @term: 天数。今日流水就传term=1，查n天内的就是传term=n
     */
    @GET("/v1/ecard/pieChart")
    fun getEcardPiechartData(@Query("cardnum") cardnum: String = EcardPref.ecardUserName, @Query("password") password: String = EcardPref.ecardPassword, @Query("term") term: Int = 1): Deferred<CommonBody<List<EcardPiechartDataBean>>>

    companion object : EcardService by ServiceFactory()
}

data class EcardProfileBean(
        val name: String,
        val balance: String, // 余额
        val cardnum: String,
        val cardstatus: String,
        val expiry: String, // 学生卡到期时间
        val subsidy: String, // 补助
        val amount: String
)

data class EcardTotalConsumptionBean(
        val total_day: Double,
        val total_month: Double
)

data class TransactionListWrapper(
        val consumption: List<TransactionInfo>,
        val recharge: List<TransactionInfo>
)

data class TransactionInfo(
        val amount: String,
        val balance: String,
        val date: String,
        val location: String,
        val time: String
)

data class ProblemBean(
        val content: String,
        val date: String,
        val id: Int,
        val title: String
)

data class EcardPiechartDataBean(
        val total: Double,
        val type: String
)

var isBindECardBoolean = EcardPref.ecardUserName != "*" && EcardPref.ecardUserName != "" && EcardPref.ecardPassword != "*" && EcardPref.ecardPassword != ""

val isBindECardLiveData = MutableLiveData<Boolean>().apply {
    value = isBindECardBoolean
}

