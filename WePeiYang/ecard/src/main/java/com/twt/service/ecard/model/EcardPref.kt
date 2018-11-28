package com.twt.service.ecard.model

import com.twt.wepeiyang.commons.experimental.preference.hawk

object EcardPref {
    var ecardUserName by hawk("ECARD_USER_NAME", "")
    var ecardPassword by hawk("ECARD_PASSWORD", "")
}
