package com.twt.service.schedule2.model

import com.twt.service.schedule2.extensions.*

/**
 * Created by retrox on 2018/3/26.
 * 按照标准的TJU课表返回格式即可用次工具类处理
 */
class CommonClassTable(val classtable: Classtable) : AbsClasstableProvider {

    val termStart: Long = classtable.termStart
    val courses: List<Course> = classtable.courses
    val dayOfInt = 86400


    override fun getCourseByDay(unixTime: Long): List<Course> {
        val dayOfWeek = getDayOfWeek(termStart, unixTime)
        val week = getWeekByTime(unixTime)
        val coursesLocal = getCourseByWeekWithTime(unixTime)
        val list = coursesLocal.onEach {
            it.arrange.trim(dayOfWeek)
        }.onEach {
            it.dayAvailable = it.isContainDay(dayOfWeek)
            it.weekAvailable = it.isWeekAvailable(weekInt = week)
        }.filter { it.dayAvailable }

        return list
    }

    override fun getCourseNextDay(unixTime: Long): List<Course> = getCourseByDay(unixTime + dayOfInt)

    override fun getTodayCourse(): List<Course> = getCourseByDay(currentUnixTime)

    override fun getTomorrowCourse(): List<Course> = getCourseNextDay(currentUnixTime)

    override fun getCourseByTime(unixTime: Long): Course? {
        TODO()
    }

    override fun getNextCourseByTime(unixTime: Long): Course {
        TODO()
    }

    override fun checkCourseConflict(course: Course): Course? = courses.findConflict(course)

    override fun getCourseByWeek(week: Int): List<Course> {
        return courses.onEach { it.refresh() } // 去除Arrange被Trim的情况
    }

    override fun getCourseByWeekWithTime(unixTime: Long): List<Course> {
        val week = getWeekByTime(unixTime)
        return getCourseByWeek(week)
    }

    override fun getWeekByTime(unixTime: Long): Int = getRealWeekInt(termStart, unixTime)

    override fun getCurrentWeek(): Int = getWeekByTime(currentUnixTime)

}