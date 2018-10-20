package com.twt.service.home.message

import com.twt.wepeiyang.commons.experimental.preference.hawk
import com.twt.wepeiyang.commons.experimental.preference.shared

object MessagePreferences {
    var isDisplayMessage by shared("is_display_message", true)
    var messageTitle by hawk("message_title", String())
    var messageContent by hawk("message_content", String())
}
