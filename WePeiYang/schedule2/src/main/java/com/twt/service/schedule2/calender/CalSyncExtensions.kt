package com.twt.service.schedule2.calender

import android.content.ContentValues
import android.graphics.Color
import android.provider.CalendarContract.Events
import com.twt.service.schedule2.extensions.even
import com.twt.service.schedule2.extensions.termStart
import com.twt.service.schedule2.model.Arrange
import com.twt.service.schedule2.model.Course
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import android.provider.CalendarContract
import com.twt.wepeiyang.commons.experimental.color.getColorCompat


/**
 * @see https://developer.android.google.cn/guide/topics/providers/calendar-provider#events
 * 用来导出到系统日历
 */
data class CalEvent(
        val title: String,
        val location: String,
        val description: String,
        val startTimeMills: Long,
        val duration: String,
        val rRule: String,
        val color: Int = 0,
        val organizer: String = "admin@wepeiyang.com" // 用作微北洋添加课程的标记
) {
    fun toContentValues(): ContentValues {
        val values = ContentValues()
        values.apply {
            put(Events.TITLE, title)
            put(Events.DTSTART, startTimeMills)
            put(Events.DESCRIPTION, description)
            put(Events.EVENT_LOCATION, location)
            put(Events.DURATION, duration)
            put(Events.RRULE, rRule)
            if (color != 0) {
                put(Events.EVENT_COLOR, color)
            }
            put(Events.ORGANIZER, organizer)
            put(Events.DTEND, null as ByteArray?)
            put(Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }
        return values
    }
}

fun Course.covertToCalEvent(): List<CalEvent> {
    val events = mutableListOf<CalEvent>()
    if (arrangeBackup.isNotEmpty()) {
        arrangeBackup.forEach {
            // 因为日历的限制，我们只能把多个无法重复的课程拆开
            val currentCourse = this.copy(arrangeBackup = listOf(it))
            val rfcCourseCalExtra = currentCourse.getRFCCalExtra()
            val calEvent = CalEvent(
                    title = currentCourse.coursename,
                    location = currentCourse.arrangeBackup[0].room,
                    description = "逻辑班号:${currentCourse.classid} 课程编号:${currentCourse.courseid}",
                    startTimeMills = rfcCourseCalExtra.startTimeMills,
                    duration = rfcCourseCalExtra.duration,
                    rRule = rfcCourseCalExtra.rRule,
                    color = courseColor
            )
            events.add(calEvent)
        }
    }
    return events
}

data class CourseCalExtra(val startTimeMills: Long, val duration: String, val rRule: String)

fun Course.getRFCCalExtra(): CourseCalExtra {
    assert(arrangeBackup.size == 1) {
        "ArrangeBackUp Size MUST be 1"
    }
    val weekUnix = 604800L
    val dayUnix = 86400L
    val arrange = arrangeBackup[0]
    val weekStartUnix = (week.start - 1) * weekUnix + termStart
    val weekEndUnix = (week.end) * weekUnix + termStart
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
    val endDateString = dateFormat.format(Date(weekEndUnix * 1000L))
    val rfcEndDate = "${endDateString}T000000Z"

    val dayUnixOffset = (arrange.day - 1) * dayUnix
    val timeOffset = offsetTimetableNew(arrange.start) // 相对当天的TimeOffset

    var courseStartUnix = weekStartUnix + dayUnixOffset + timeOffset
    if (arrange.week == "双周" && !week.start.even) {
        courseStartUnix += weekUnix
    }

    val duration = offsetTimetableNew(arrange.end, true) - timeOffset
    val durationText = "PT${duration}S"

    val rfcWeekday = getRFCWeekDay(arrange.day)

    val interval = if (arrange.week == "单双周") 1 else 2

    val rRule = "FREQ=WEEKLY;UNTIL=${rfcEndDate};INTERVAL=${interval};BYDAY=${rfcWeekday};WKST=SU"
    return CourseCalExtra(startTimeMills = courseStartUnix * 1000L,
            duration = durationText,
            rRule = rRule)
}

/**
 * 获取上课/下课时间对于当天零点的相对时间戳
 */
fun offsetTimetableNew(startNumber: Int, getEnd: Boolean = false): Int {
    var offset: Int = when (startNumber) {
        1 -> 1800
        2 -> 4800
        3 -> 8700
        4 -> 11700 // 上午四节课的开始时间
        5 -> 19800
        6 -> 22800
        7 -> 26700
        8 -> 29700 // 下午四节课
        9 -> 37800
        10 -> 40800
        11 -> 43800
        12 -> 46800
        else -> 0
    }
    offset += 3600 * 8 // 时区
    return if (getEnd) {
        offset + 2700
    } else offset
}

fun getRFCWeekDay(num: Int): String {
    val cDay = arrayOf("SU", "MO", "TU", "WE", "TH", "FR", "SA", "SU")
    return cDay[num]
}