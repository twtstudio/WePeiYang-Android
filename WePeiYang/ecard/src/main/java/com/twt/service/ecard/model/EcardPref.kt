package com.twt.service.ecard.model

import com.twt.wepeiyang.commons.experimental.preference.hawk

object EcardPref {
    const val ASSISTANCE_MARK = "assistance"
    const val KEY_PROBLEM = "problem"
    const val KEY_REISSUE = "reissue"
    var ecardUserName by hawk("ECARD_USER_NAME", "*")
    var ecardPassword by hawk("ECARD_PASSWORD", "*")
    var ecardHistorySpinnerIndex by hawk("ECARD_HISTORY_SPINNER_INDEX", 0)
    var ecardHistoryLength by hawk("ECARD_HISTORY_LENGTH", 7)
}