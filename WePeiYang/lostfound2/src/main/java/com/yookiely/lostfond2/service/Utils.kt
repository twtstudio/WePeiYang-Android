package com.yookiely.lostfond2.service

import android.Manifest

class Utils {
    val PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

    companion object {
        const val ALL_TYPE = 0
        const val ALL_TIME = 5
        const val TYPE_OF_FOUND = 1
        const val TABLE_NAME = "myTable"
        const val ID = "_id"
        const val CONTENT = "content"

        fun getType(i: Int) = when (i) {
            0 -> "全部"
            1 -> "身份证"
            2 -> "饭卡"
            3 -> "手机"
            4 -> "钥匙"
            5 -> "书包"
            6 -> "手表&饰品"
            7 -> "水杯"
            8 -> "U盘&硬盘"
            9 -> "钱包"
            10 -> "银行卡"
            11 -> "书"
            12 -> "伞"
            13 -> "其他"
            else -> "wrong_type"
        }

        fun getDetailFilterOfTime(i: Int) = when (i) {
            1 -> "全部时间"
            2 -> "一天之内"
            3 -> "1 - 7天"
            4 -> "7 - 15天"
            5 -> "15 - 30天"
            else -> "wrong_time"
        }

        fun getDetailFilterOfPlace(i: Int) = when (i) {
            1 -> "北洋园"
            2 -> "卫津路"
            else -> "wrong_place"
        }

        fun getPicUrl(addUrl: String) = "http://open.twtstudio.com/$addUrl"
//        fun getPicUrl(addUrl: String) = "http://open-lostfound.twtstudio.com/$addUrl"

        fun getExit(i: Int) = when (i) {
            0 -> " "
            1 -> "A口"
            2 -> "B口"
            else -> ""
        }
    }
}