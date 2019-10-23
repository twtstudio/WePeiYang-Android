package com.twtstudio.retrox.auth.api

import com.tencent.bugly.crashreport.CrashReport
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.experimental.service.AuthSelfBean
import com.twt.wepeiyang.commons.experimental.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


val authSelfLocalCache = Cache.hawk<AuthSelfBean>("AUTH_SELF")
val authSelfRemoteCache = Cache.from(AuthService.Companion::authSelf)
val authSelfLiveData = RefreshableLiveData.use(authSelfLocalCache, authSelfRemoteCache) {
    CommonPreferences.twtuname = it.twtuname
    CommonPreferences.studentid = it.studentid
    CommonPreferences.isBindLibrary = it.accounts.lib
    CommonPreferences.isBindTju = it.accounts.tju
    CommonPreferences.dropOut = it.dropout
    CommonPreferences.realName = it.realname
    CrashReport.setUserId(it.twtuname)
}

fun login(username: String, password: String, callback: suspend (RefreshState<Unit>) -> Unit = {}) {
    GlobalScope.launch(Dispatchers.Main) {
        AuthService.getToken(username, password).awaitAndHandle {
            callback(RefreshState.Failure(it))
        }?.data?.let {
            CommonPreferences.token = it.token
            CommonPreferences.password = password
            CommonPreferences.isLogin = true
            callback(RefreshState.Success(Unit))
        }
    }
}

fun refreshToken(callback: suspend (RefreshState<Unit>) -> Unit = {})  {
    GlobalScope.launch(Dispatchers.Main) {
        AuthService.refreshToken().awaitAndHandle {
            callback(RefreshState.Failure(it))
        }?.data?.let {
            CommonPreferences.token = it.token
        }
    }
}
