package com.twt.service.announcement.service

import com.twt.wepeiyang.commons.experimental.preference.hawk

object AnnoPreference {

    var searchHistory: MutableSet<String> by hawk("ques_search_history", mutableSetOf())


}