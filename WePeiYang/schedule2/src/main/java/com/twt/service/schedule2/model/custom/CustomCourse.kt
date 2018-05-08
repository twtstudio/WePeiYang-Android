package com.twt.service.schedule2.model.custom

import android.arch.persistence.room.*
import com.twt.service.schedule2.model.*

@Entity(primaryKeys = ["name", "teacher"], tableName = "table_custom_course")
data class CustomCourse(
        val name: String,
        val teacher: String,
        val ext: String,/*自定义课程的额外信息*/
        val arrange: List<Arrange>,
        val week: Week
)

@Dao
interface CustomCourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourses(vararg courses: CustomCourse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourse(course: CustomCourse)

    /**
     * @return Although usually not necessary, you can have this method return an int value instead, indicating the number of rows removed from the database.
     */
    @Delete
    fun deleteCourses(vararg course: CustomCourse): Int

    @Query("SELECT * FROM table_custom_course")
    fun loadAllCourses(): List<CustomCourse>
}



