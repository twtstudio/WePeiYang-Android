package com.tjuwhy.yellowpages2.service

import com.twt.wepeiyang.commons.experimental.preference.hawk

object YellowPagePreference {

    var searchHistory: MutableSet<String> by hawk("search_history", mutableSetOf())

    var phoneBean: PhoneBean? by hawk("phoneBean", null)

    var collectionList: Array<SubData> by hawk("collection_list", arrayOf())

    var subArray: Array<Array<SubData>> by hawk("sub_data_list", arrayOf(arrayOf()))

}
