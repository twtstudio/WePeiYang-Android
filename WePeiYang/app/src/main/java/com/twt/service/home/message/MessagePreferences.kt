package com.twt.service.home.message

import com.twt.wepeiyang.commons.experimental.preference.hawk
import com.twt.wepeiyang.commons.experimental.preference.shared

object MessagePreferences {
    var isDisplayMessage by shared("is_display_message", false)
    var messageTitle by hawk("message_title", "")
    var messageContent by hawk("message_content", "")
    var messageVersion by hawk("message_version",0)
    var gtclietid by hawk("getui_clientid","")
    var messageNum by hawk("getui_messagenum",0)
}
