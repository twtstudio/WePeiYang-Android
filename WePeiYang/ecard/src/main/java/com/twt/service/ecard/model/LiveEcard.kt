package com.twt.service.ecard.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.twt.wepeiyang.commons.experimental.cache.Cache
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.cache.hawk
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch

/**
 * 感觉设计的还行...
 */
data class ECardFullInfo(
        val ecardProfile: EcardProfileBean, // 个人基本信息
        val transactionList: List<TransactionInfo>,
        val totalCost: EcardTotalConsumptionBean,
        val cache: Boolean
)

val ecardCacheKey = "ECARD_FULL_INFO_CACHE"
val ecardFullInfoCache = Cache.hawk<ECardFullInfo>(ecardCacheKey)

object LiveEcardManager {

    private val eCardFullInfoLiveData = object : MutableLiveData<RefreshState<ECardFullInfo>>() {
        override fun onActive() {
            super.onActive()
            refreshEcardFullInfo()
        }
    }


    fun getEcardLiveData(): LiveData<RefreshState<ECardFullInfo>> = eCardFullInfoLiveData

    init {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            val cache = ecardFullInfoCache.get().await()
            cache?.let { eCardFullInfoLiveData.postValue(RefreshState.Success(it.copy(cache = true))) } // 第一次Load Cache
        }
    }

    fun refreshEcardFullInfo(forceReload: Boolean = true) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            eCardFullInfoLiveData.postValue(RefreshState.Failure(throwable))
        }

        GlobalScope.launch(Dispatchers.Main + coroutineExceptionHandler) {

            eCardFullInfoLiveData.postValue(RefreshState.Refreshing())

            if (!forceReload) {
                ecardFullInfoCache.get().await()?.let { eCardFullInfoLiveData.postValue(RefreshState.Success(it)) } // Load Cache
            }

            val profileDeferred = EcardService.getEcardProfile()
            val historyDeferred = EcardService.getEcardTransaction()
            val totalDeferred = EcardService.getEcardTotalConsumption()

            val profile = profileDeferred.await().data
                    ?: throw IllegalStateException("校园卡状态数据为空 联系开发者解决")
            val history = historyDeferred.await().data
                    ?: throw IllegalStateException("校园卡历史数据为空 联系开发者解决")
            val total = totalDeferred.await().data
                    ?: throw IllegalStateException("校园卡历史数据为空 联系开发者解决")

            val eCardFullInfo = ECardFullInfo(ecardProfile = profile, transactionList = history, totalCost = total, cache = false)
            ecardFullInfoCache.set(eCardFullInfo)
            eCardFullInfoLiveData.postValue(RefreshState.Success(eCardFullInfo))
        }
    }

}
