package xyz.rickygao.gpa2.service

import com.twt.wepeiyang.commons.experimental.preference.shared

object GpaPreferences {
    var isDisplayGpa by shared("pref_is_display_gpa", false)

    var isBoomGpa by shared("pref_is_boom_gpa", false)
}