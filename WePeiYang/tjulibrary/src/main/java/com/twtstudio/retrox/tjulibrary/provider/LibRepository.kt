package com.twtstudio.retrox.tjulibrary.provider

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import com.twt.wepeiyang.commons.network.RetrofitProvider
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Created by retrox on 27/10/2017.
 */
object LibRepository {
    private const val USER_INFO = "LIB_USER_INFO"
    private val libApi = RetrofitProvider.getRetrofit().create(LibApi::class.java)

    fun getUserInfo(refresh: Boolean = false): LiveData<Info> {
        val livedata = MutableLiveData<Info>()
        async(UI) {
            if (!refresh) {
                val cacheData: Info? = bg { Hawk.get<Info>(USER_INFO) }.await()
                cacheData?.let {
                    livedata.value = it
                }
            }

            val networkData: Info? = bg { libApi.libUserInfo.map { it.data }.toBlocking().first() }.await()
            networkData?.let {
                livedata.value = it
                bg { Hawk.put(USER_INFO, networkData) }
            }

        }
        return livedata
    }

}