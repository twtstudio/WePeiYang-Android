package com.yookiely.lostfond2.service

import android.Manifest
import com.orhanobut.hawk.Hawk
import com.twt.wepeiyang.commons.experimental.preference.hawk

object Utils {
    val PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

    const val ALL_TYPE = 0
    const val ALL_TIME = 5
    const val TYPE_OF_FOUND = 1
    const val ID_KEY = "id"
    const val LOSTORFOUND_KEY = "lostOrFound"
    const val DETAIL_TYPE = "type"
    const val INDEX_KEY = "index"
    const val SEARCH_LIST_KEY = "lf_search"
    const val QUERY_KEY = "query"
    var needRefresh = false
    var campus: Int? by hawk("campus", null)
    const val FOUND = 1
    const val LOST = 2
    const val CAMPUS_BEI_YANG_YUAN = 1
    const val CAMPUS_WEI_JIN_LU = 2
    const val STRING_FOUND = "found"
    const val STRING_LOST = "lost"

    private val typeMap = hashMapOf(0 to "全部", 1 to "身份证",
            2 to "饭卡",3 to "手机",4 to "钥匙",5 to "书包",6 to "手表&饰品",7 to "水杯",
            8 to "U盘&硬盘", 9 to "钱包", 10 to "银行卡",11 to "书",12 to "伞",13 to "其他")

    private val timeMap = hashMapOf( 1 to "全部时间", 2 to "一天之内",
            3 to "1 - 7天", 4 to "7 - 15天", 5 to "15 - 30天")

    fun getType(i: Int) = typeMap[i].orEmpty()

    fun getDetailFilterOfTime(i: Int) = timeMap[i].orEmpty()

    fun getDetailFilterOfPlace(i: Int) = when (i) {
        1 -> "北洋园"
        2 -> "卫津路"
        else -> "wrong_place"
    }

    //        fun getPicUrl(addUrl: String) = "http://open.twtstudio.com/$addUrl"
    fun getPicUrl(addUrl: String) = "http://open-lostfound.twtstudio.com/$addUrl"

    fun getExit(i: Int?) = when (i) {
        0 -> ""
        1 -> "A口"
        2 -> "B口"
        else -> ""
    }

    fun getGarden(i: String?): String = when (i) {
        "无" -> ""
        "1斋", "2斋", "3斋" -> "格园"
        "6斋", "7斋", "8斋" -> "诚园"
        "9斋", "10斋" -> "正园"
        "11斋", "12斋" -> "修园"
        "13斋", "14斋", "15斋", "16斋" -> "齐园"
        else -> ""
    }

    //以下几个方法是关于领取站点的spinner的一系列内容处理
    fun getListOfRoom(list: MutableList<String>, intList: MutableList<Int>, position: Int) {
        list.clear()
        intList.clear()
        when (position) {
            0 -> {
                list.add("无")
                intList.add(0)
            }
            1 -> {
                list.add("1斋")
                list.add("2斋")
                list.add("3斋")
                intList.add(1)
                intList.add(2)
                intList.add(3)
            }
            2 -> {
                list.add("6斋")
                list.add("7斋")
                list.add("8斋")
                intList.add(6)
                intList.add(7)
                intList.add(8)
            }
            3 -> {
                list.add("13斋")
                list.add("14斋")
                list.add("15斋")
                list.add("16斋")
                intList.add(13)
                intList.add(14)
                intList.add(15)
                intList.add(16)
            }
//                3 -> {
//                    list.add("9斋")
//                    list.add("10斋")
//                    intList.add(9)
//                    intList.add(10)
//                }
//                4 -> {
//                    list.add("11斋")
//                    list.add("12斋")
//                    intList.add(11)
//                    intList.add(12)
//                }
//                5 -> {
//                    list.add("13斋")
//                    list.add("14斋")
//                    list.add("15斋")
//                    list.add("16斋")
//                    intList.add(13)
//                    intList.add(14)
//                    intList.add(15)
//                    intList.add(16)
//                }
            else -> {
            }
        }
    }

    fun getListOfEntrance(list: MutableList<String>, intList: MutableList<Int>, room: Int) {
        list.clear()
        intList.clear()

        when (room) {
            0 -> {
                list.add("无")
                intList.add(0)
            }
            1, 2, 9, 10 -> {
                list.add("只有一个入口")
                intList.add(0)
            }
            11, 12 -> {
                list.add("只可A口")
                intList.add(0)
            }
            else -> {
                list.add("A口")
                list.add("B口")
                intList.add(1)
                intList.add(2)
            }
        }
    }

    // 返回值数据为其在所对应数组中的位置
    fun getPositionOfGarden(i: String?): Int = when (i) {
        "无" -> 0
        "1斋", "2斋", "3斋" -> 1
        "6斋", "7斋", "8斋" -> 2
        "13斋", "14斋", "15斋", "16斋" -> 3
//            "9斋", "10斋" -> 3
//            "11斋", "12斋" -> 4
//            "13斋", "14斋", "15斋", "16斋" -> 5
        else -> 0
    }

    fun getPositionOfRoom(i: String?): Int = when (i) {
        "无", "1斋", "6斋", "9斋", "11斋", "13斋" -> 0
        "2斋", "7斋", "10斋", "12斋", "14斋" -> 1
        "3斋", "8斋", "15斋" -> 2
        "16斋" -> 3
        else -> 0
    }

    fun getPositionOfEntrance(i: Int?): Int = when (i) {
        0, 1 -> 0
        2 -> 1
        else -> 0
    }
}