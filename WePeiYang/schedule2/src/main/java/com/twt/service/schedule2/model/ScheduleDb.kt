package com.twt.service.schedule2.model

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import com.twt.service.schedule2.model.audit.*
import com.twt.service.schedule2.model.custom.CustomCourse
import com.twt.service.schedule2.model.custom.CustomCourseDao
import com.twt.wepeiyang.commons.experimental.CommonContext

object ScheduleDb {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `table_college_course` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `collegeName` TEXT NOT NULL, PRIMARY KEY(`id`))")
            database.execSQL("CREATE TABLE IF NOT EXISTS `table_college` (`collegeName` TEXT NOT NULL, `collegeId` INTEGER NOT NULL, PRIMARY KEY(`collegeId`))")
        }
    }

    private val scheduleDatabase: ScheduleDataBase = creteDb()

    val customCourseDao = scheduleDatabase.customCourseDao()

    val auditCourseDao = scheduleDatabase.auditCourseDao()


    private inline fun <reified T : RoomDatabase?> creteDb(): T {
        return Room.databaseBuilder(CommonContext.application.applicationContext, T::class.java, "ScheduleDB").addMigrations(MIGRATION_1_2).build()
    }

    @Database(entities = [CustomCourse::class, AuditCourse::class, CollegeCourse::class, AuditCollegeData::class], version = 2, exportSchema = false)
    @TypeConverters(ArrangeListTypeConverter::class, WeekTypeConverter::class, AuditInfoItemTypeConverter::class)
    abstract class ScheduleDataBase : RoomDatabase() {
        public abstract fun customCourseDao(): CustomCourseDao

        public abstract fun auditCourseDao(): AuditCourseDao
    }


}
