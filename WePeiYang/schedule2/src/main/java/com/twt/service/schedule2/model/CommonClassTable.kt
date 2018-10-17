package com.twt.service.schedule2.model

import android.util.Log
import com.tencent.bugly.Bugly
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.*
import com.twt.service.schedule2.model.duplicate.DuplicateCourseManager

/**
 * Created by retrox on 2018/3/26.
 * 按照标准的TJU课表返回格式即可用次工具类处理
 */
class CommonClassTable(val classtable: Classtable) : AbsClasstableProvider {

    val termStart: Long = classtable.termStart
    val courses: List<Course> = classtable.courses
    val dayOfInt = 86400

    init {
        try {
            classtable.courses.map {
                it.copy()
            }.onEach { course ->
                val result = course.arrange.groupBy {
                    it.week + it.day.toString()
                }.filter {
                    it.value.size > 1 // 看看是不是超了
                }
                if (result.isNotEmpty()) {
                    val list = result.values.first().toMutableList()
                    val day = list.first().day
                    val finalList  = list.filter { it.day == day }.toMutableList()
                    finalList.trim(day)
                    if (finalList.size > 1) {
                        finalList.removeAt(0)
                    }
                    val duplicateCourse = course.copy(arrangeBackup = finalList).apply {
                        refresh()
                    }
                    DuplicateCourseManager.addCourse(duplicateCourse)
                }
                Log.d("CourseTest", result.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getCourseByDay(unixTime: Long): List<Course> {
        val dayOfWeek = getDayOfWeek(termStart, unixTime)
        val week = getWeekByTime(unixTime)
        val coursesLocal = getCourseByWeekWithTime(unixTime)
        val list = coursesLocal.map {
            it.copy() // 妈的巨坑 辣鸡代码 Debug心态都崩了
            // 这个东西需要进行一次软拷贝 因为arrange字段是Mutable 所以在多次查询的时候就会烂 GG
        }.onEach {
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