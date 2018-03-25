package com.twt.service

import com.twt.wepeiyang.commons.experimental.preference.shared

object AppPreferences {

    var isDisplayGpa by shared("pref_is_display_gpa", false)

    var isBoomGpa by shared("pref_is_boom_gpa", false)

    var isDisplayBike by shared("pref_is_display_bike", false)

    var isShowTodayCourse by shared("pref_is_show_today_course", true)

    var isAutoCheckUpdate by shared("pref_is_auto_check_update", true)

}