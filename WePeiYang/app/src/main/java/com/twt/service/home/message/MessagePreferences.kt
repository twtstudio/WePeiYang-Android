package com.twt.service.home.message

import com.twt.wepeiyang.commons.experimental.preference.hawk
import com.twt.wepeiyang.commons.experimental.preference.shared

object MessagePreferences {
    // todo Oct.20th 18 by tjwhm: key 的命名也应通过下划线或驼峰的规范命名来避免拼写检查的提示
    var isDisplayMessage by shared("pref_is_display_message", true)
    var messageTitle by hawk("messagetitle", String())
    var messageContent by hawk("messagecontent", String())
}
