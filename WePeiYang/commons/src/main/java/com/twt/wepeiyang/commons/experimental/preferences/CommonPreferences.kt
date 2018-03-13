package com.twt.wepeiyang.commons.experimental.preferences

import com.orhanobut.hawk.Hawk

/**
 * A persistent preferences' holder using Hawk or defaultSharedPreferences.
 */
object CommonPreferences {

    var token by hawk("token", "")

    var gpaToken by hawk("gpa_token", "")

    var isLogin by hawk("is_login", false)

    var startUnix by hawk("start_unix", 946656000L)

    var studentid by hawk("student_number", "")

    var isBindTju by hawk("is_bind_tju", false)

    var isBindLibrary by hawk("is_bind_library", false)

    var isBindBike by hawk("is_bind_bike", false)

    var isFirstLogin by hawk("is_first_login", true)

    var isAcceptTos by hawk("is_accept_tos", false)

    var dropOut by hawk("drop_out", 0)

    var twtuname by hawk("user_id", "")

    var proxyAddress by hawk("proxy_address", "")

    var proxyPort by hawk("proxy_port", 0)

    var isDisplayGpa by shared("pref_is_display_gpa", true)

    var isDisplayBike by shared("pref_is_display_bike", true)

    var isShowTodayCourse by shared("pref_is_show_today_course", true)

    var isAutoCheckUpdate by shared("pref_is_auto_check_update", true)

    fun clear() = Hawk.deleteAll()

}
