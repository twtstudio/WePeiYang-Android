package com.twt.service.schedule2

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twt.service.schedule2.extensions.todayAvailable
import com.twt.service.schedule2.extensions.todayUnavailable
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.CommonClassTable
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * Created by retrox on 2018/3/27.
 */
class TjuClasstableTest {
    val gson: Gson = Gson()
    val classtableType = object : TypeToken<CommonBody<Classtable>>() {}.type
    val dayOfInt = 86400L

    lateinit var classtable: CommonBody<Classtable>
    lateinit var tjuClassTable: CommonClassTable

    @Before
    fun init() {
        classtable = gson.fromJson(ConstantData.Schedule3, classtableType)
        assertNotNull(classtable.data)
        tjuClassTable = CommonClassTable(classtable = classtable.data!!)
    }

    @Test
    fun testWeek4Day2() {
        val currentUnix: Long = 1522082777L // 4周 周二
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        assertEquals(3, todayCourses.todayAvailable.size)
        assertEquals(4, todayCourses.todayUnavailable.size)
    }

    @Test
    fun testWeek4Day1() {
        val currentUnix = 1522082777L - dayOfInt // 4周周一
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)

        assertEquals(3, todayCourses.todayAvailable.size)
        assertEquals(1, todayCourses.todayUnavailable.size)
    }

    @Test
    fun testWeek4Day3() {
        val currentUnix = 1522082777L + dayOfInt // 4周周3
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        assertEquals(4, todayCourses.todayAvailable.size)
        assertEquals(3, todayCourses.todayUnavailable.size)
    }

    @Test
    fun testWeek4Day4() {
        val currentUnix = 1522082777L + dayOfInt * 2 // 4周周4
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        assertEquals(4, todayCourses.todayAvailable.size)
        assertEquals(3, todayCourses.todayUnavailable.size)
    }

    @Test
    fun testWeek4Day5() {
        val currentUnix = 1522082777L + dayOfInt * 3 // 4周周5
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        assertEquals(1, todayCourses.todayAvailable.size)
        assertEquals(2, todayCourses.todayUnavailable.size)
    }

    @Test
    fun testWeek4Day6() {
        val currentUnix = 1522082777L + dayOfInt * 4 // 4周周6
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        assertEquals(todayCourses.todayAvailable.size, 0)
        assertEquals(todayCourses.todayUnavailable.size, 0)
    }

    @Test
    fun testWeek4Day7() {
        val currentUnix = 1522082777L + dayOfInt * 5 // 4周周6
        val todayCourses = tjuClassTable.getCourseByDay(currentUnix)
        assertEquals(todayCourses.todayAvailable.size, 0)
        assertEquals(todayCourses.todayUnavailable.size, 0)
    }

}