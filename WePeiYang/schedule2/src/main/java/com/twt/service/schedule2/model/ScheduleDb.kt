package com.twt.service.schedule2.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.twt.service.schedule2.model.custom.CustomCourse
import com.twt.service.schedule2.model.custom.CustomCourseDao
import com.twt.wepeiyang.commons.experimental.CommonContext

object ScheduleDb {

    private val scheduleDatabase: ScheduleDataBase = creteDb()

    val customCourseDao = scheduleDatabase.customCourseDao()

    private inline fun <reified T : RoomDatabase?> creteDb(): T {
        return Room.databaseBuilder(CommonContext.application.applicationContext, T::class.java, "ScheduleDB").build()
    }

    @Database(entities = [CustomCourse::class], version = 1, exportSchema = false)
    @TypeConverters(ArrangeListTypeConverter::class,WeekTypeConverter::class)
    abstract class ScheduleDataBase : RoomDatabase() {
        public abstract fun customCourseDao(): CustomCourseDao
    }

}
