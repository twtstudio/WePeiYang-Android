package com.twt.service.ecard.model

import com.twt.wepeiyang.commons.experimental.preference.hawk

object EcardPref {
    const val ASSISTANCE_MARK = "assistance"
    const val KEY_PROBLEM = "problem"
    const val KEY_REISSUE = "reissue"
    const val INDEX_KEY = "history"
    const val PRE_CHART = "preciouschart"
    const val PRE_LIST = "preciouslist"
    const val IS_RECHARGE = 1
    const val IS_CONSUME = 2
    var ecardUserName by hawk("ECARD_USER_NAME", "*")
    var ecardPassword by hawk("ECARD_PASSWORD", "*")
    var ecardHistorySpinnerIndex by hawk("ECARD_HISTORY_SPINNER_INDEX", 0)
    var ecardHistoryLength by hawk("ECARD_HISTORY_LENGTH", 7)
}