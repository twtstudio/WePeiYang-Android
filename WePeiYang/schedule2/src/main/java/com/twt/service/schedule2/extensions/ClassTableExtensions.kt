package com.twt.service.schedule2.extensions

import com.twt.service.schedule2.model.AbsClasstableProvider
import java.util.*

/**
 * Created by retrox on 2018/3/26.
 * 课程表相关拓展
 */

fun AbsClasstableProvider.getRealWeekInt(startUnix: Long, weekUnix: Long): Int {
    val span = weekUnix - startUnix
    val day = (span / 86400).toInt()
    return day / 7 + 1
}

fun AbsClasstableProvider.getDayOfWeek(startUnix: Long, dayUnix: Long): Int {
    val weeks = getRealWeekInt(startUnix, dayUnix) - 1 // 比如说我现在是第四周 我完全度过的是前三周 所以要减1
    val weekTime = weeks * 60 * 60 * 24 * 7
    val span = dayUnix - startUnix
    val timeLeft = span - weekTime
    return (timeLeft/(60 * 60 * 24) + 1).toInt()
}

val AbsClasstableProvider.currentUnixTime
    get() = Calendar.getInstance().timeInMillis / 1000

val Int.even
    get() = this % 2 == 0

fun getWeekString(week: Int): String {
    val cDay = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
    var sWeek = ""
    if (week / 10 != 0 || week == 0) {
        if (week / 10 != 1) {
            sWeek += cDay[week / 10]
        }
        if (week != 0) {
            sWeek += "十"
        }
    }
    if (week % 10 != 0) {
        sWeek += cDay[week % 10]
    }
    return sWeek
}

fun getChineseCharacter(num: Int): String {
    val cDay = arrayOf("零", "一", "二", "三", "四", "五", "六", "日")
    return cDay[num]
}
