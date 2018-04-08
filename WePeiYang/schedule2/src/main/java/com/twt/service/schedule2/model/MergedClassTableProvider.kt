package com.twt.service.schedule2.model

import com.twt.service.schedule2.extensions.currentUnixTime
import com.twt.service.schedule2.extensions.findConflict
import com.twt.service.schedule2.extensions.mergeCourses

class MergedClassTableProvider(
        val tjuClassTable: AbsClasstableProvider,
        val auditClassTable: AbsClasstableProvider,
        val customCourseTable: AbsClasstableProvider
) : AbsClasstableProvider {

    val totalCoursesList = getCourseByWeek(3) // 姑且就当做第三周吧 反正只是CheckConfict用

    val dayOfInt = 86400

    override fun getCourseByDay(unixTime: Long): List<Course> {
        return mergeCourses(
                tjuClassTable.getCourseByDay(unixTime),
                auditClassTable.getCourseByDay(unixTime),
                customCourseTable.getCourseByDay(unixTime),
                dayUnix = unixTime
        )
    }

    override fun getCourseNextDay(unixTime: Long): List<Course> = getCourseByDay(unixTime + dayOfInt)

    override fun getTodayCourse(): List<Course> = getCourseByDay(currentUnixTime)

    override fun getTomorrowCourse(): List<Course> = getCourseNextDay(currentUnixTime)

    override fun getCourseByTime(unixTime: Long): Course? {
        TODO("开发人员表示并不想做这个功能")
    }

    override fun getNextCourseByTime(unixTime: Long): Course {
        TODO("开发人员表示并不想做这个功能")
    }

    override fun checkCourseConflict(course: Course): Course? = totalCoursesList.findConflict(course)

    override fun getCourseByWeek(week: Int): List<Course> {
        return mutableListOf<Course>().apply {
            addAll(tjuClassTable.getCourseByWeek(week))
            addAll(auditClassTable.getCourseByWeek(week))
            addAll(customCourseTable.getCourseByWeek(week))
            // 其他getCourseByWeek的时候就已经做了refresh操作
        }
    }

    override fun getCourseByWeekWithTime(unixTime: Long): List<Course> {
        val week = getWeekByTime(unixTime)
        return getCourseByWeek(week)
    }

    override fun getCurrentWeek(): Int = tjuClassTable.getCurrentWeek()

    override fun getWeekByTime(unixTime: Long): Int = tjuClassTable.getWeekByTime(unixTime)
}