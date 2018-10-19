package com.twt.service.home.message

import com.twt.wepeiyang.commons.experimental.preference.hawk
import com.twt.wepeiyang.commons.experimental.preference.shared

object MessagePreferences {
    var isDisplayMessage by shared("pref_is_display_message", true)
    var messageTitle by hawk("messagetitle", String())
    var messageContent by hawk("messagecontent", String())
}