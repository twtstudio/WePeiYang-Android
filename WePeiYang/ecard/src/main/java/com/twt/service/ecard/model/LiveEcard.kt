package com.twt.service.ecard.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.twt.service.ecard.window.ECardInfoPop
import com.twt.wepeiyang.commons.experimental.cache.Cache
import com.twt.wepeiyang.commons.experimental.cache.hawk
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

/**
 * 感觉设计的不是很好...
 */
data class ECardFullInfo(val personInfo: ECardPersonInfo, val transactionInfoList: List<TransactionInfo>, val todayCost: Float, val cache: Boolean)

val ecardCacheKey = "ECARD_FULL_INFO_CACHE"
val ecardFullInfoCache = Cache.hawk<ECardFullInfo>(ecardCacheKey)

object LiveEcardManager {
    private val eCardFullInfoLiveData = object : MutableLiveData<ECardFullInfo>() {
        override fun onActive() {
            super.onActive()
            refreshEcardFullInfo()
        }
    }

    private val ecardExceptionLiveData = MutableLiveData<Throwable>()

    // 两个数据的输出 一个是正常数据 一个输出异常
    fun getEcardLiveData(): LiveData<ECardFullInfo> = eCardFullInfoLiveData

    fun getEcardExceptionLiveData(): LiveData<Throwable> = ecardExceptionLiveData

    fun refreshEcardFullInfo(forceReload: Boolean = false) {

        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            ecardExceptionLiveData.postValue(throwable)
        }

        launch(UI + coroutineExceptionHandler) {

            if (!forceReload) {
                val cache = ecardFullInfoCache.get().await()
                cache?.let { eCardFullInfoLiveData.postValue(it.copy(cache = true)) } // 拉一下缓存先 因为这个好歹要看看
            }

            async(CommonPool) {
                login(EcardPref.ecardUserName, EcardPref.ecardPassword)
            }.await()
            val personInfoDeferred = async(CommonPool) {
                fetchPersonInfo()
            }
            val historyDeffered = async(CommonPool) {
                fetchHistory()
            }
            val personInfo = personInfoDeferred.await()
            val todayCost = historyDeffered.await().today().fold(0f) { prev: Float, transactionInfo: TransactionInfo ->
                prev + transactionInfo.amount.toFloat()
            }

            val fullInfo = ECardFullInfo(personInfo, historyDeffered.await(), todayCost, false)
            ecardFullInfoCache.set(fullInfo) // 更新缓存

            eCardFullInfoLiveData.postValue(fullInfo)
        }

    }
}