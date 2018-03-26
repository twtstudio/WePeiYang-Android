package com.twt.service.schedule2

import com.twt.service.schedule2.extensions.*
import com.twt.service.schedule2.model.AbsClasstableProvider
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.mock


/**
 * Created by retrox on 2018/3/27.
 */
class ExtTest {
    val startUnix: Long = 1520179200L
    val currentUnix: Long = 1522082777L //第四周

    val classtable = mock(AbsClasstableProvider::class.java)

    @Test
    fun testEven() {
        assertTrue(2.even)
        assertFalse(3.even)
        assertTrue(18.even)
        assertFalse(7.even)
    }

    @Test
    fun testWeekInt() {
        val weekInt = classtable.getRealWeekInt(startUnix, currentUnix)
        assertEquals(weekInt, 4)
    }

    @Test
    fun testWeekToString() {
        assertEquals(getWeekString(10), "十")
        assertEquals(getWeekString(15), "十五")
        assertEquals(getWeekString(8), "八")
        assertEquals(getWeekString(0), "零")
    }

    @Test
    fun testDayOfWeek() {
        assertEquals(classtable.getDayOfWeek(startUnix,currentUnix),2)
        assertEquals(classtable.getDayOfWeek(startUnix,1522170061L),3)
        assertEquals(classtable.getDayOfWeek(startUnix,1525107661L),2)
    }
}