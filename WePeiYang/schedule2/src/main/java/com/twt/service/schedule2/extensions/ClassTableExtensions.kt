package com.twt.service.schedule2.extensions

import com.twt.service.schedule2.model.AbsClasstableProvider
import com.twt.service.schedule2.model.Arrange
import com.twt.service.schedule2.model.Course
import java.time.DayOfWeek
import java.util.*

/**
 * Created by retrox on 2018/3/26.
 * 课程表相关拓展
 */

val termStart: Long = 1520179200L // 测试时暂时不从SP中读取

fun AbsClasstableProvider.getRealWeekInt(startUnix: Long = termStart, weekUnix: Long): Int {
    val span = weekUnix - startUnix
    val day = (span / 86400).toInt()
    return day / 7 + 1
}

/**
 * 获取一周中的第几天 周一 -> 1
 */
fun AbsClasstableProvider.getDayOfWeek(startUnix: Long = termStart, dayUnix: Long): Int {
    val weeks = getRealWeekInt(startUnix, dayUnix) - 1 // 比如说我现在是第四周 我完全度过的是前三周 所以要减1
    val weekTime = weeks * 60 * 60 * 24 * 7
    val span = dayUnix - startUnix
    val timeLeft = span - weekTime
    return (timeLeft / (60 * 60 * 24) + 1).toInt()
}

val AbsClasstableProvider.currentUnixTime
    get() = Calendar.getInstance().timeInMillis / 1000

/**
 * 用于获取当天课程之后的操作 -> 获取真正在今天上课的课程 反映为彩色课程
 */
val List<Course>.todayAvailable: MutableList<Course>
    get() = this.filter { it.dayAvailable && it.weekAvailable }.toMutableList()

/**
 * 用于获取当天课程之后的操作 -> 获取在本天但是非本周的课程 反映为彩色课程
 */
val List<Course>.todayUnavailable: List<Course>
    get() {
        val today = todayAvailable
        return this.filterNot { today.contains(it) }
    }

/**
 * 课表查找冲突
 * @param course 传入课程 看看列表里面有没有和他冲突的
 * @param dayOfWeek 冲突检测基于星期
 */
fun List<Course>.findConflict(course: Course, dayOfWeek: Int): Course? {
    // 先检查今天有没有课 如果有那么就筛选一次Arrange
    if (!course.arrange.any { it.day == dayOfWeek }) {
        return null
    } else {
        course.arrange.trim(dayOfWeek)
    }
    val courseArrange = course.arrange[0]

    this.filter { it.arrange.any { it.day == dayOfWeek } }.onEach {
        it.arrange.trim(dayOfWeek)
    }.onEach {
        if (it.arrange[0].checkConflict(courseArrange)) return it
    }
    return null
}

/**
 * 课表查找冲突
 * @param course 传入课程 看看列表里面有没有和他冲突的
 */
fun List<Course>.findConflict(course: Course): Course? {
    val dayList = course.arrange.map { it.day }
    dayList.forEach {
        this.findConflict(course, it)?.let {
            return it
        }
    }
    return null
}

fun MutableList<Arrange>.trim(dayOfWeek: Int) {
    this.retainAll {
        it.day == dayOfWeek
    }
    kotlin.assert(this.size == 1) {
        "这不科学... size应该是1啊"
    }
}

fun Arrange.checkConflict(arrange: Arrange): Boolean {
    if (week != arrange.week) return false
    if (day != arrange.day) return false
    return arrange.start in start..end || start in arrange.start..arrange.end
}

/**
 * 来自多个课程列表的课程合并
 * @param courseList 可变参数 输入多个课程列表
 * @param dayUnix 时间的时间戳
 * @return 返回合并后的课程 列表中为直接显示的课程 里面存在链表存放着冲突的课程
 */
fun AbsClasstableProvider.mergeCourses(vararg courseList: MutableList<Course>, dayUnix: Long): List<Course> {
    val dayOfWeek = getDayOfWeek(dayUnix = dayUnix)
    val availableCourses = courseList.map { it.todayAvailable }
            .reduce { acc, list ->
                list.forEach { course ->
                    val conflictFatherCourse = acc.findConflict(course, dayOfWeek)
                    if (conflictFatherCourse != null) {
                        conflictFatherCourse.next.add(course)
                    } else {
                        acc.add(course)
                    }
                }
                acc
            }

    val unAvailableCourse = courseList.map { it.todayUnavailable }.flatMap { it }
    unAvailableCourse.forEach { course ->
        val conflictFatherCourse = availableCourses.findConflict(course, dayOfWeek)
        if (conflictFatherCourse != null) {
            conflictFatherCourse.next.add(course)
        } else {
            availableCourses.add(course)
        }
    }
    return availableCourses
}

/**
 * 是不是偶数啊 判断单双周用
 */
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
