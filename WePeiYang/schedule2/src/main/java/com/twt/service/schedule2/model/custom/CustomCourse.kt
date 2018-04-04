package com.twt.service.schedule2.model.custom

import android.arch.persistence.room.*
import com.twt.service.schedule2.model.*

@Entity(primaryKeys = ["name","teacher"],tableName = "table_custom_course")
data class CustomCourse(
        val name: String,
        val teacher: String,
        @TypeConverters(ArrangeListTypeConverter::class) val arrange: List<Arrange>,
        @TypeConverters(WeekTypeConverter::class) val week: Week
)

@Dao
interface CustomCourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourses(vararg courses: CustomCourse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourse(course: CustomCourse)

    @Query("SELECT * FROM table_custom_course")
    fun loadAllCourses(): List<CustomCourse>
}

fun addCustomCourse(name: String,teacher: String, arrange: List<Arrange>, week: Week) {
    val customCourse = CustomCourse(name, teacher, arrange, week)
    addCustomCourse(customCourse)
}

fun addCustomCourse(customCourse: CustomCourse, dao: CustomCourseDao = ScheduleDb.customCourseDao) {
    dao.insertCourse(customCourse)
}

