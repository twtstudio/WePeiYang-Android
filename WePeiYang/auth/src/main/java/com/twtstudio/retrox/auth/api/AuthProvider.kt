package com.twtstudio.retrox.auth.api

import android.arch.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import com.tencent.bugly.crashreport.CrashReport
import com.twt.wepeiyang.commons.experimental.api.AuthSelfBean
import com.twt.wepeiyang.commons.experimental.api.RealAuthService
import com.twt.wepeiyang.commons.experimental.extensions.ConsumableMessage
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.pref.CommonPreferences
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg

object AuthProvider {

    val authSelfLiveData = MutableLiveData<AuthSelfBean>()
    val successLiveData = MutableLiveData<ConsumableMessage<String>>()
    val errorLiveData = MutableLiveData<ConsumableMessage<String>>()

    private val handler = CoroutineExceptionHandler { _, it ->
        errorLiveData.value = ConsumableMessage("好像出了什么问题，${it.javaClass} ${it.message}")
    }

    const val FROM_LOGIN = 0
    fun login(username: String, password: String, quiet: Boolean = false) {
        launch(UI + if (quiet) QuietCoroutineExceptionHandler else handler) {
            val remote = RealAuthService.getToken(username, password)

            remote.await().data?.let {
                CommonPreferences.token = it.token
                CommonPreferences.isLogin = true

                if (!quiet) successLiveData.value = ConsumableMessage("登录成功啦", from = FROM_LOGIN)
            }

        }.invokeOnCompletion {
            it?.let {
                if (!quiet) errorLiveData.value = ConsumableMessage("好像出了什么问题，${it.message}", from = FROM_LOGIN)
            }
        }
    }

    const val FROM_AUTH_SELF = 1
    private const val HAWK_KEY_AUTH_SELF = "AUTH_SELF"
    fun authSelf(useCache: Boolean = true, quiet: Boolean = false) {
        launch(UI + handler) {
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

                if (!quiet) successLiveData.value = ConsumableMessage("成功拉取到个人状态", from = FROM_AUTH_SELF)
                Hawk.put(HAWK_KEY_AUTH_SELF, it)
            }

        }
    }

}
