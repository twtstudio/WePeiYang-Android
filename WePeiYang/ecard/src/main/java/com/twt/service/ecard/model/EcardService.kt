package com.twt.service.ecard.model

import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by tjwhm@TWTStudio at 1:48 AM, 2018/11/29.
 * Happy coding!
 */


interface EcardService {
    @GET("v1/ecard/profile")
    fun getEcardProfile(@Query("cardnum") cardnum: String, @Query("password") password: String): Deferred<CommonBody<EcardProfileBean>>

    companion object : EcardService by ServiceFactory()
}

fun getEcardProfile(callback: suspend (RefreshState<CommonBody<EcardProfileBean>>) -> Unit) =
        launch(UI) {
            EcardService.getEcardProfile(EcardPref.ecardUserName, EcardPref.ecardPassword).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

data class EcardProfileBean(
        val balance: String,
        val cardnum: String,
        val cardstatus: String,
        val expiry: String,
        val subsidy: String
)
