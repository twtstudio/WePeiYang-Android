package com.twt.service.schedule2

import android.arch.persistence.room.Room
import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.twt.service.schedule2.model.Arrange
import com.twt.service.schedule2.model.ScheduleDb
import com.twt.service.schedule2.model.Week
import com.twt.service.schedule2.model.custom.*

import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var customCourseDao: CustomCourseDao
    private lateinit var database: ScheduleDb.ScheduleDataBase
    val tag = "Schedule2Debug-Database"

    @Before fun init() {
        val appContext = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(appContext,ScheduleDb.ScheduleDataBase::class.java).build()
        customCourseDao = database.customCourseDao()
    }

    @Test
    fun testDB() {
        val arrange1 = Arrange("单双周",3,4,3,"学校")
        val week = Week(1,18)
        val course1 = createCourse("给女朋友买零食","开心的我", listOf(arrange1),week,"必须要去.... ")
//        CustomCourseManager.addCustomCourse(course1,customCourseDao)
        customCourseDao.insertCourse(course1)
        assertEquals(1,customCourseDao.loadAllCourses().size)
//        CustomCourseManager.addCustomCourse(course1,customCourseDao)
        customCourseDao.insertCourse(course1)
        assertEquals(1,customCourseDao.loadAllCourses().size)

        val course2 = createCourse("给自己买零食","开心的我", listOf(arrange1),week)
        customCourseDao.insertCourse(course2)
        assertEquals(2,customCourseDao.loadAllCourses().size)

        customCourseDao.loadAllCourses().forEach {
//            println(it.arrange)
            Log.d(tag,it.toString())
        }

        val deleteResult = customCourseDao.deleteCourses(course1)
        assertEquals(1,deleteResult)
        assertEquals(1,customCourseDao.loadAllCourses().size)
    }

    @After @Throws(IOException::class) fun close() {
        database.close()
    }

    fun createCourse(name: String, teacher: String, arrange: List<Arrange>, week: Week,ext: String = ""): CustomCourse {
        val customCourse = CustomCourse(name, teacher,ext, arrange, week)
        return customCourse
    }
}
