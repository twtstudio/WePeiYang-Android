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

    var isBindTju by hawk("is_bind_tju", false)

    var isBindLibrary by hawk("is_bind_library", false)

    var isBindBike by hawk("is_bind_bike", false)

    var isAcceptTos by hawk("is_accept_tos", false)

    var dropOut by hawk("drop_out", 0)

    var twtuname by hawk("user_id", "")

    var password by hawk("user_pwd", "")

    var realName by hawk("user_realname", "")

    var proxyAddress by hawk("proxy_address", "")

    var proxyPort by hawk("proxy_port", 0)

    var customThemeIndex by hawk("custom_theme_index", 0)

    var tjuuname by hawk("tju_id", "")
    var tjupwd by hawk("tju_pwd", "")
    fun clear() {
        Hawk.deleteAll()
        CommonContext.defaultSharedPreferences.edit().clear().apply()
    }

}
