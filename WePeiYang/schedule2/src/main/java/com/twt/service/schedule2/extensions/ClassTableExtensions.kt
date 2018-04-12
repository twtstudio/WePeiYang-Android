package com.twt.service.schedule2.extensions

import com.twt.service.schedule2.model.*
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by retrox on 2018/3/26.
 * 课程表相关拓展
 */

val termStart: Long  // 测试时暂时不从SP中读取 todo: SP存取
    get() {
        var result = 1520179200L
        try {
            result = SchedulePref.termStart // 因为要兼容测试无法获取Pref的情况
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

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

/**
 * 把一天的课程 添加上空课
 */
fun List<Course>.flatDay(dayOfWeek: Int, forceFill: Boolean = false): List<Course> {

    fun createEmptyCourse(start: Int, end: Int, day: Int = dayOfWeek) = Course(
            coursename = "空",
            week = Week(0, 0),
            arrangeBackup = listOf(Arrange(week = "单双周", start = start, end = end, day = day))
    ).apply {
        refresh()
    }
    // 用来标识空课程
    // 用空课程中的start和end来标识数据 因为需要-> 点击空白课程获取数据用 + 当前视图的week参数

    val trimedList = this.onEach { it.arrange.trim(dayOfWeek) }.sortedBy { it.arrange[0].start }
    val realList = mutableListOf<Course>()
    val totalCourseNumber = 12
    if ((trimedList.isEmpty() && dayOfWeek != 6 && dayOfWeek != 7) || (trimedList.isEmpty()&&forceFill)) {
        // 万一出现了这种周六日只有其中一天有课的 就需要强制填充... 避免出现奇怪的现象
        for (i in 1..totalCourseNumber) {
            realList.add(createEmptyCourse(i, i))
        }
    }
    trimedList.forEachIndexed { index, course ->
        val start = course.arrange[0].start
        val end = course.arrange[0].end

        if (index == 0) {
            for (i in 1 until start) {
                realList.add(createEmptyCourse(start = i, end = i))
            }
            realList.add(course)
        } else {
            val lastStart = trimedList[index - 1].arrange[0].start
            val lastEnd = trimedList[index - 1].arrange[0].end
            if (start - lastEnd > 0) {
                for (i in lastEnd + 1 until start) {
                    realList.add(createEmptyCourse(start = i, end = i))
                }
            }
            realList.add(course)
        }

        if (index == trimedList.size - 1 && end < totalCourseNumber) { // 最后一个 并且还要添加emptyCourse
            for (i in end + 1..totalCourseNumber) {
                realList.add(createEmptyCourse(start = i, end = i))
            }
        }

    }
    return realList
}

/**
 * 应该在最后的Merge之后用
 * @see AbsClasstableProvider.getCourseByDay 就是说 这个方法里面本身包含MergeCourses操作的时候
 * 才可以使用此方法来获取当周课程
 */
fun AbsClasstableProvider.getWeekCourseFlated(weekInt: Int, startUnix: Long = termStart): List<List<Course>> {
    val wrapperList = mutableListOf<List<Course>>()
    val offset = 3600L // 加一个偏移量... 因为按照0点计算不保险
    val dayOfSeconds = 86400L
    val startUnixWithOffset = startUnix + offset + (weekInt-1) * dayOfSeconds * 7
    val dayUnixList = mutableListOf<Long>() // 一周内每天的时间戳
    for (i in 0..6) {
        dayUnixList.add(startUnixWithOffset + dayOfSeconds * i)
    }
    // 周六日其中一个有课才要Force填充
    val willForceFill: Boolean = !(this.getCourseByDay(dayUnixList[5]).isEmpty() && this.getCourseByDay(dayUnixList[6]).isEmpty())
    dayUnixList.mapIndexed { index, l ->
        val dayOfWeek = index + 1
        this.getCourseByDay(l).flatDay(dayOfWeek, forceFill = willForceFill)
    }.forEach {
        wrapperList.add(it)
    }
    return wrapperList
}

/**
 * 根据星期几来筛选掉其他的Arrange 一般在获取当天课程的时候用
 */
fun MutableList<Arrange>.trim(dayOfWeek: Int) {
    if (this.size < 2) return
    this.retainAll {
        it.day == dayOfWeek
    }
    kotlin.assert(this.size == 1) {
        "这不科学... size应该是1啊"
    }
}

fun Arrange.checkConflict(arrange: Arrange): Boolean {
//    if (week == "单周" && arrange.week == "双周") return false
//    if (week == "双周" && arrange.week == "单周") return false
//    蛋疼 如果单双周就不能用这个直接判断了
    if (day != arrange.day) return false
    return arrange.start in start..end || start in arrange.start..arrange.end
}

/**
 * 来自多个课程列表的课程合并
 * @param courseList 可变参数 输入多个课程列表
 * @param dayUnix 时间的时间戳
 * @return 返回合并后的课程 列表中为直接显示的课程 里面存在链表存放着冲突的课程
 */
fun AbsClasstableProvider.mergeCourses(vararg courseList: List<Course>, dayUnix: Long): List<Course> {
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
