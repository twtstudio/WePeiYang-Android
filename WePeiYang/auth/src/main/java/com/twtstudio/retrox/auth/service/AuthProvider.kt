package com.twtstudio.retrox.auth.service

import android.arch.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import com.tencent.bugly.crashreport.CrashReport
import com.twt.wepeiyang.commons.experimental.CommonPreferences
import com.twt.wepeiyang.commons.experimental.extensions.ConsumableMessage
import com.twt.wepeiyang.commons.experimental.service.AuthSelfBean
import com.twt.wepeiyang.commons.experimental.service.RealAuthService
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Created by retrox on 2017/2/20.
 */

object AuthProvider {

    val authSelfLiveData = MutableLiveData<AuthSelfBean>()
    val successLiveData = MutableLiveData<ConsumableMessage<String>>()
    val errorLiveData = MutableLiveData<ConsumableMessage<String>>()

    const val FROM_LOGIN = 0
    fun login(username: String, password: String, silent: Boolean = false) {
        async(UI) {
            val remote = RealAuthService.getToken(username, password)

            remote.await().data?.let {
                CommonPreferences.token = it.token
                CommonPreferences.isLogin = true
                CommonPreferences.isFirstLogin = false

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

            val remote = RealAuthService.authSelf()

            if (useCache) {
                bg {
                    Hawk.get<AuthSelfBean?>(HAWK_KEY_AUTH_SELF, null)
                }.await()?.let {
                    authSelfLiveData.value = it

                    CommonPreferences.twtuname = it.twtuname
                    CommonPreferences.studentid = it.studentid
                    CommonPreferences.isBindLibrary = it.accounts.lib
                    CommonPreferences.isBindTju = it.accounts.tju
                    CommonPreferences.dropOut = it.dropout
                    CrashReport.setUserId(it.twtuname)
                }
            }

            remote.await().let {
                authSelfLiveData.value = it

                CommonPreferences.twtuname = it.twtuname
                CommonPreferences.studentid = it.studentid
                CommonPreferences.isBindLibrary = it.accounts.lib
                CommonPreferences.isBindTju = it.accounts.tju
                CommonPreferences.dropOut = it.dropout
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
