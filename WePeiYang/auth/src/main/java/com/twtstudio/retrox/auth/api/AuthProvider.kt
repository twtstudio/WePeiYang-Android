package com.twtstudio.retrox.auth.api

import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


val authSelfLocalCache = Cache.hawk<NewAuthSelfBean>("AUTH_SELF")
val authSelfRemoteCache = Cache.from(AuthApi.Companion::newAuthSelf).map(AuthBody<NewAuthSelfBean>::result)
val authSelfLiveData = RefreshableLiveData.use(authSelfLocalCache, authSelfRemoteCache) {
    CommonPreferences.twtuname = it.nickname
    CommonPreferences.studentid = it.userNumber
    CommonPreferences.realName = it.realname
    CommonPreferences.token = it.token
}

fun login(username: String, password: String, callback: suspend (RefreshState<Unit>) -> Unit = {}) {
    GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
        AuthApi.login(username, password).awaitAndHandle {
            callback(RefreshState.Failure(it))
        }?.let {
            when (it.error_code) {
                40002 -> callback(RefreshState.Failure(Throwable("用户不存在，请前往注册页面进行账户注册")))
                40004 -> callback(RefreshState.Failure(Throwable("用户名或密码错误")))
                50003 -> callback(RefreshState.Failure(Throwable("未绑定的url，请联系管理员")))
                0 -> {
                    CommonPreferences.password = password
                    CommonPreferences.token = it.result?.token.toString()
                    CommonPreferences.isLogin = true
                    if (it.result?.telephone == null) {
                        callback(RefreshState.Failure(Throwable("请前往信息完善界面完善手机号等相关信息")))
                    } else {
                        callback(RefreshState.Success(Unit))
                    }
                }
            }
        }
    }
}

fun getCaptcha(phoneNumber: String, callback: suspend (RefreshState<Unit>) -> Unit = {}) {
    GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
        AuthApi.getCaptcha(phoneNumber).awaitAndHandle {
            callback(RefreshState.Failure(it))
        }?.let {
            when (it.error_code) {
                50009 -> callback(RefreshState.Failure(Throwable("手机号码无效")))
                50010 -> callback(RefreshState.Failure(Throwable("发送失败")))
                50011 -> callback(RefreshState.Failure(Throwable("验证失败")))
                50012 -> callback(RefreshState.Failure(Throwable("电子邮件或手机号格式不规范")))
                50013 -> callback(RefreshState.Failure(Throwable("电子邮件或手机号重复")))
                50014 -> callback(RefreshState.Failure(Throwable("手机号已存在")))
                0 -> callback(RefreshState.Success(Unit))
            }
        }
    }
}

fun register(studentId: String, username: String, phoneNumber: String, captcha: String, password: String, email: String, personalID: String, callback: suspend (RefreshState<Unit>) -> Unit = {}) {
    GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
        AuthApi.register(studentId, username, phoneNumber, captcha, password, email, personalID).awaitAndHandle {
            callback(RefreshState.Failure(it))
        }?.let {
            when (it.error_code) {
                50005 -> callback(RefreshState.Failure(Throwable("学号和身份证号不匹配")))
                50006, 50007, 50008 -> callback(RefreshState.Failure(Throwable("用户名或邮箱已存在")))
                50009 -> callback(RefreshState.Failure(Throwable("手机号码无效")))
                50010 -> callback(RefreshState.Failure(Throwable("发送失败")))
                50011 -> callback(RefreshState.Failure(Throwable("验证失败")))
                50012 -> callback(RefreshState.Failure(Throwable("电子邮件或手机号格式不规范")))
                50013 -> callback(RefreshState.Failure(Throwable("电子邮件或手机号重复")))
                50014 -> callback(RefreshState.Failure(Throwable("手机号已存在")))
                0 -> callback(RefreshState.Success(Unit))
            }
        }
    }
}

fun infoSupplement(phoneNumber: String, captcha: String, email: String, callback: suspend (RefreshState<Unit>) -> Unit = {}) {
    GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
        AuthApi.infoSupple(phoneNumber, captcha, email).awaitAndHandle {
            callback(RefreshState.Failure(it))
        }?.let {
            when (it.error_code) {
                50005 -> callback(RefreshState.Failure(Throwable("学号和身份证号不匹配")))
                50006, 50007, 50008 -> callback(RefreshState.Failure(Throwable("用户名或邮箱已存在")))
                50009 -> callback(RefreshState.Failure(Throwable("手机号码无效")))
                50010 -> callback(RefreshState.Failure(Throwable("发送失败")))
                50011 -> callback(RefreshState.Failure(Throwable("验证失败")))
                50012 -> callback(RefreshState.Failure(Throwable("电子邮件或手机号格式不规范")))
                50013 -> callback(RefreshState.Failure(Throwable("电子邮件或手机号重复")))
                50014 -> callback(RefreshState.Failure(Throwable("手机号已存在")))
                0 -> {
                    CommonPreferences.telephone = phoneNumber
                    callback(RefreshState.Success(Unit))
                }
            }
        }
    }
}
