package com.twt.service.schedule2

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twt.service.schedule2.extensions.findConflict
import com.twt.service.schedule2.extensions.mergeCourses
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.TjuClassTable
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * 纯办公网课表的课程表Merge情况
 */
class ClassMergeTest {

    val dayOfInt = 86400L

    val classtable: CommonBody<Classtable> = TableProvider.classtable
    val tjuClassTable: TjuClassTable = TableProvider.tjuClassTable

    @Test
    fun testMergeWeek4Day2() {
        val currentUnix: Long = 1522082777L // 4周 周二
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        val mergedClasses = tjuClassTable.mergeCourses(todayCourses, dayUnix = currentUnix)
        Assert.assertEquals(5, mergedClasses.size)
        Assert.assertEquals(1, mergedClasses.filter { it.next.size > 0 }.size)
    }

    @Test
    fun testMergeWeek4Day3() {
        val currentUnix: Long = 1522082777L + dayOfInt // 4周 周二
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        val mergedClasses = tjuClassTable.mergeCourses(todayCourses, dayUnix = currentUnix)
        Assert.assertEquals(5, mergedClasses.size)
        Assert.assertEquals(3, mergedClasses.filter { it.next.size > 0 }.size)
    }

    @Test
    fun testMergeWeek4Day1() {
        val currentUnix: Long = 1522082777L - dayOfInt // 4周 周二
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        val mergedClasses = tjuClassTable.mergeCourses(todayCourses, dayUnix = currentUnix)
        Assert.assertEquals(4, mergedClasses.size)
        Assert.assertEquals(1, mergedClasses.filter { it.next.size > 0 }.size)
    }

    @Test
    fun testMergeWeek4Day4() {
        val currentUnix: Long = 1522082777L + 2 * dayOfInt // 4周 周4
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        val mergedClasses = tjuClassTable.mergeCourses(todayCourses, dayUnix = currentUnix)
        Assert.assertEquals(5, mergedClasses.size)
        Assert.assertEquals(1, mergedClasses.filter { it.next.size > 0 }.size)
    }

    @Test
    fun testMergeWeek4Day5() {
        val currentUnix: Long = 1522082777L + 3 * dayOfInt // 4周 周5
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        val mergedClasses = tjuClassTable.mergeCourses(todayCourses, dayUnix = currentUnix)
        Assert.assertEquals(1, mergedClasses.size)
        Assert.assertEquals(1, mergedClasses.filter { it.next.size > 0 }.size)
    }

    @Test
    fun testMergeWeek4Day6() {
        val currentUnix: Long = 1522082777L + 4 * dayOfInt // 4周 周6
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        val mergedClasses = tjuClassTable.mergeCourses(todayCourses, dayUnix = currentUnix)
        Assert.assertEquals(1, mergedClasses.size)
        Assert.assertEquals(0, mergedClasses.filter { it.next.size > 0 }.size)
    }

    @Test
    fun testMergeWeek4Day7() {
        val currentUnix: Long = 1522082777L + 5 * dayOfInt // 4周 周7
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        val mergedClasses = tjuClassTable.mergeCourses(todayCourses, dayUnix = currentUnix)
        Assert.assertEquals(1, mergedClasses.size)
        Assert.assertEquals(0, mergedClasses.filter { it.next.size > 0 }.size)
    }

    val week5Day1 = 1522602061L // 第五周第一天
    @Test
    fun testWeek5Day1() {
        val currentUnix = week5Day1
        val todayCourse = tjuClassTable.getCourseByDay(currentUnix)
        val mergedClasses = tjuClassTable.mergeCourses(todayCourse, dayUnix = currentUnix)
        assertEquals(4, mergedClasses.size)
        assertEquals(1, mergedClasses.filter { it.next.size > 0 }.size)
    }

    @Test
    fun testWeek5Day2() {
        val currentUnix = week5Day1 + dayOfInt
        val todayCourse = tjuClassTable.getCourseByDay(currentUnix)
        val mergedClasses = tjuClassTable.mergeCourses(todayCourse, dayUnix = currentUnix)
        assertEquals(5, mergedClasses.size)
        assertEquals(1, mergedClasses.filter { it.next.size > 0 }.size)
    }

    @Test
    fun testWeek5Day3() {
        val currentUnix = week5Day1 + dayOfInt*2
        val todayCourse = tjuClassTable.getCourseByDay(currentUnix)
        val mergedClasses = tjuClassTable.mergeCourses(todayCourse, dayUnix = currentUnix)
        assertEquals(5, mergedClasses.size)
        assertEquals(3, mergedClasses.filter { it.next.size > 0 }.size)
    }

    @Test
    fun testWeek5Day4() {
        val currentUnix = week5Day1 + dayOfInt*3
        val todayCourse = tjuClassTable.getCourseByDay(currentUnix)
        val mergedClasses = tjuClassTable.mergeCourses(todayCourse, dayUnix = currentUnix)
        assertEquals(5, mergedClasses.size)
        assertEquals(1, mergedClasses.filter { it.next.size > 0 }.size)
    }

    @Test fun testConflict() {
        val currentUnix = week5Day1 + dayOfInt
        val todayCourse = tjuClassTable.getCourseByDay(currentUnix)
        val mergedClasses = tjuClassTable.mergeCourses(todayCourse, dayUnix = currentUnix)
        val course = mergedClasses[2]
        println(course)
        val conflictCouse = tjuClassTable.courses.findConflict(course)
        println(conflictCouse)
        assertNotNull(conflictCouse)
    }

}