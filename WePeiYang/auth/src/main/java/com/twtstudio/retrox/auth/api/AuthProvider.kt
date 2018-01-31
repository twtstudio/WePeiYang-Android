package com.twtstudio.retrox.auth.api

import android.arch.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import com.tencent.bugly.crashreport.CrashReport
import com.twt.wepeiyang.commons.network.RetrofitProvider
import com.twt.wepeiyang.commons.utils.CommonPrefUtil
import com.twtstudio.retrox.auth.ext.ConsumableMessage
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Created by retrox on 2017/2/20.
 */

object AuthProvider {

    private val api = RetrofitProvider.getRetrofit().create(AuthApi::class.java)
    val authSelfLiveData = MutableLiveData<AuthSelfBean>()
    val successLiveData = MutableLiveData<ConsumableMessage<String>>()
    val errorLiveData = MutableLiveData<ConsumableMessage<String>>()

    const val FROM_LOGIN = 0
    fun login(username: String, password: String, silent: Boolean = false) {
        async(UI) {
            val remote = bg {
                api.login(username, password).toBlocking().value()?.data
            }

            remote.await()?.let {
                CommonPrefUtil.setToken(it.token)
                CommonPrefUtil.setIsLogin(true)
                CommonPrefUtil.setIsFirstLogin(false)

                if (!silent) successLiveData.value = ConsumableMessage("登录成功啦", from = FROM_LOGIN)
            }

        }.invokeOnCompletion {
            it?.let {
                if (!silent) errorLiveData.value = ConsumableMessage("好像出了什么问题，${it.message}", from = FROM_LOGIN)
            }
        }
    }

    const val FROM_AUTH_SELF = 1
    private const val HAWK_KEY_AUTH_SELF = "AUTH_SELF"
    fun authSelf(useCache: Boolean = true, silent: Boolean = false) {

        async(UI) {

            val remote = bg {
                api.authSelf().toBlocking().value()
            }

            if (useCache) {
                bg {
                    Hawk.get<AuthSelfBean?>(HAWK_KEY_AUTH_SELF, null)
                }.await()?.let {
                    authSelfLiveData.value = it

                    CommonPrefUtil.setStudentNumber(it.studentid)
                    CommonPrefUtil.setIsBindLibrary(it.accounts.lib)
                    CommonPrefUtil.setIsBindTju(it.accounts.tju)
                    CommonPrefUtil.setDropOut(it.dropout)
                    CrashReport.setUserId(it.twtuname)
                }
            }

            remote.await()?.let {
                authSelfLiveData.value = it

                CommonPrefUtil.setStudentNumber(it.studentid)
                CommonPrefUtil.setIsBindLibrary(it.accounts.lib)
                CommonPrefUtil.setIsBindTju(it.accounts.tju)
                CommonPrefUtil.setDropOut(it.dropout)
                CrashReport.setUserId(it.twtuname)

                if (!silent) successLiveData.value = ConsumableMessage("成功拉取到个人状态", from = FROM_AUTH_SELF)
                Hawk.put(HAWK_KEY_AUTH_SELF, it)
            }

        }.invokeOnCompletion {
            it?.let {
                if (!silent) errorLiveData.value = ConsumableMessage("好像出了什么问题，${it.message}", from = FROM_AUTH_SELF)
            }
        }

    }

}
