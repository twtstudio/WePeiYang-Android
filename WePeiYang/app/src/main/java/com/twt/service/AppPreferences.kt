package com.twt.service

import com.twt.wepeiyang.commons.experimental.preferences.shared

object AppPreferences {

    var isDisplayGpa by shared("pref_is_display_gpa", true)

    var isDisplayBike by shared("pref_is_display_bike", true)

    var isShowTodayCourse by shared("pref_is_show_today_course", true)

    var isAutoCheckUpdate by shared("pref_is_auto_check_update", true)

}