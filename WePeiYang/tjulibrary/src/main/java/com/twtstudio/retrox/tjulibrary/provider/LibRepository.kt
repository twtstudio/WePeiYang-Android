package com.twtstudio.retrox.tjulibrary.provider

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import com.twt.wepeiyang.commons.network.RetrofitProvider
import com.twt.wepeiyang.commons.network.RxErrorHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Created by retrox on 27/10/2017.
 */
object LibRepository {
    private const val USER_INFO = "LIB_USER_INFO"
    private val libApi = RetrofitProvider.getRetrofit().create(LibApi::class.java)

    fun getUserInfo(refresh: Boolean = false, errorHandler: (Throwable) -> Unit = RxErrorHandler()::call): LiveData<Info> {
        val livedata = MutableLiveData<Info>()
        GlobalScope.async(Dispatchers.Main) {
            if (!refresh) {
                val cacheData: Info? = withContext(Dispatchers.Default) { Hawk.get<Info>(USER_INFO) }
                cacheData?.let {
                    livedata.value = it
                }
            }

            val networkData: Info? = withContext(Dispatchers.Default) { libApi.libUserInfo.map { it.data }.toBlocking().first() }
            networkData?.let {
                livedata.value = it
                withContext(Dispatchers.Default) { Hawk.put(USER_INFO, networkData) }
            }
        }.invokeOnCompletion {
            it?.let { errorHandler(it) }
        }
        return livedata
    }

}