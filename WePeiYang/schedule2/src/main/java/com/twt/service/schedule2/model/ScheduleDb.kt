package com.twt.service.schedule2.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.twt.service.schedule2.model.audit.AuditCourse
import com.twt.service.schedule2.model.audit.AuditCourseDao
import com.twt.service.schedule2.model.audit.AuditInfoItemTypeConverter
import com.twt.service.schedule2.model.custom.CustomCourse
import com.twt.service.schedule2.model.custom.CustomCourseDao
import com.twt.wepeiyang.commons.experimental.CommonContext

object ScheduleDb {

    private val scheduleDatabase: ScheduleDataBase = creteDb()

    val customCourseDao = scheduleDatabase.customCourseDao()

    val auditCourseDao = scheduleDatabase.auditCourseDao()

    private inline fun <reified T : RoomDatabase?> creteDb(): T {
        return Room.databaseBuilder(CommonContext.application.applicationContext, T::class.java, "ScheduleDB").build()
    }

    @Database(entities = [CustomCourse::class, AuditCourse::class], version = 1, exportSchema = false)
    @TypeConverters(ArrangeListTypeConverter::class, WeekTypeConverter::class, AuditInfoItemTypeConverter::class)
    abstract class ScheduleDataBase : RoomDatabase() {
        public abstract fun customCourseDao(): CustomCourseDao

        public abstract fun auditCourseDao(): AuditCourseDao
    }

}
