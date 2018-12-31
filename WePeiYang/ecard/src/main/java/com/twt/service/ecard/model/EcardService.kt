package com.twt.service.ecard.model

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.google.gson.annotations.Expose
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
     * @type: 1 -> 充值查询 2 -> 消费查询
     * @day: 查多少天的数据 超出饭卡补办期会导致异常
     */
    @GET("v1/ecard/transaction")
    fun getEcardTransaction(@Query("cardnum") cardnum: String = EcardPref.ecardUserName, @Query("password") password: String = EcardPref.ecardPassword, @Query("day") day: Int = 2, @Query("type") type: Int = 2): Deferred<CommonBody<TransactionListWrapper>>

    companion object : EcardService by ServiceFactory()
}

data class EcardProfileBean(
        val balance: String,
        val cardnum: String,
        val cardstatus: String,
        val expiry: String,
        val subsidy: String,
        val amount: String
)

data class TransactionListWrapper(val transaction: List<TransactionInfo>)

fun EcardProfileBean.castToECardPersonInfo() = ECardPersonInfo(number = cardnum, status = cardstatus, balance = balance, validityPeriod = expiry, notReceivedMoney = subsidy)

data class TransactionInfo(val date: String, val time: String, val location: String, val amount: String, val balance: String, @Expose(serialize = false, deserialize = false) var isCost: Boolean? = true)

var isBindECardBoolean = EcardPref.ecardUserName != "*" && EcardPref.ecardUserName != "" && EcardPref.ecardPassword != "*" && EcardPref.ecardPassword != ""

val isBindECardLiveData = MutableLiveData<Boolean>().apply {
    value = isBindECardBoolean
}

