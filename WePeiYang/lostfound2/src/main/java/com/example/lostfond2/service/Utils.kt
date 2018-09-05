package com.example.lostfond2.service

import android.Manifest

class Utils {
    val PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

    companion object {
        fun getType(i: Int) = when (i) {
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


        fun getFilter(i: Int) = when (i) {
            1 -> "时间"
            2 -> "校区"
            else -> "wrong_filter"
        }

        fun getDetailFilterOfTime(i : Int) = when (i) {
            1 -> "一天之内"
            2 -> "1 - 7天"
            3 -> "7 - 15天"
            4 -> "15 - 30天"
            else -> "wrong_time"
        }

        fun getDetailFilterOfPlace(i : Int) = when (i) {
            1 -> "北洋园校区"
            2 -> "卫津路校区"
            else -> "wrong_place"
        }

        fun getPicUrl(addUrl: String) = "http://open.twtstudio.com/{$addUrl}"


        fun noPicForDetail()= "http://open.twtstudio.com/uploads/17-07-12/945139dcd91e9ed3d5967ef7f81e18f6.jpg"

    }

}
