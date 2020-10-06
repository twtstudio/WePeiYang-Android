package com.twt.service.announcement.service

import com.twt.wepeiyang.commons.experimental.preference.hawk

object AnnoPreference {

    var searchHistory: MutableSet<String> by hawk("ques_search_history", mutableSetOf())

    // myId指本机用户的ID，应该只在进入校务专区的时候请求一次
    var myId: Int? by hawk("myId", null)
}