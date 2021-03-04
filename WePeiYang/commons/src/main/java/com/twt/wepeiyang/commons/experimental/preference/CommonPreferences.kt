package com.twt.wepeiyang.commons.experimental.preference

import com.orhanobut.hawk.Hawk
import com.twt.wepeiyang.commons.experimental.CommonContext

/**
 * A persistent preferences' holder using Hawk or defaultSharedPreferences.
 */
object CommonPreferences {

    var token by hawk("token", "")

    var isLogin by hawk("is_login", false)

    var startUnix by hawk("start_unix", 946656000L)

    var studentid by hawk("student_number", "")

    var isBindBike by hawk("is_bind_bike", false)

    var twtuname by hawk("user_id", "")

    var password by hawk("user_pwd", "")

    var realName by hawk("user_realname", "")

    var proxyAddress by hawk("proxy_address", "")

    var proxyPort by hawk("proxy_port", 0)

    var customThemeIndex by hawk("custom_theme_index", 0)

    var tjuuname by hawk("tju_id_spider", "")// 办公网账号
    var tjupwd by hawk("tju_pwd_spider", "")// 办公网密码
    var tjulogin:Boolean? by hawk("tju_login_spider",null) // 办公网登陆状态是否有效, true: 已登录，false: 登录过期，null: 从未登录过，无登录状态
    fun clear() {
        Hawk.deleteAll()
        CommonContext.defaultSharedPreferences.edit().clear().apply()
    }

    var isUserInfoUpdated by hawk("user_info_update", false)

    var telephone by hawk("user_phone_number", "")
    var stuType by hawk("user_stu_type", "")

    var isFer by hawk("first_login", true)
}
