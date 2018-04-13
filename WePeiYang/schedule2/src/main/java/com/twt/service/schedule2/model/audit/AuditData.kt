package com.twt.service.schedule2.model.audit

import android.arch.persistence.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.twt.service.schedule2.model.Arrange
import com.twt.service.schedule2.model.Course
import com.twt.service.schedule2.model.Week
import java.lang.reflect.Type

@Entity(primaryKeys = ["courseId"], tableName = "table_audit_course")
data class AuditCourse(val college: String,
                       @SerializedName("course_id") val courseId: Int,
                       @SerializedName("course_name") val courseName: String,
                       val year: String,
                       val semester: String,
                       val infos: List<InfoItem>)

@Dao
interface AuditCourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAuditCourses(vararg courses: AuditCourse)

    @Delete
    fun deleteAuditCourse(vararg course: AuditCourse): Int

    @Query("SELECT * FROM table_audit_course")
    fun loadAllAuditCourses(): List<AuditCourse>
}

data class InfoItem(@SerializedName("course_id") val courseId: String,
                    @SerializedName("end_week") val endWeek: Int,
                    @SerializedName("course_name") val courseName: String,
                    @SerializedName("week_type") val weekType: Int,
                    @SerializedName("course_id_in_tju") val courseIdInTju: String,
                    @SerializedName("start_week") val startWeek: Int,
                    val building: String,
                    val room: String,
                    @SerializedName("start_time") val startTime: Int,
                    val teacher: String,
                    @SerializedName("course_length") val courseLength: Int,
                    @SerializedName("week_day") val weekDay: Int,
                    @SerializedName("teacher_type") val teacherType: String,
                    val id: Int)

/**
 * 将蹭课Course转化成普通Course来兼容通用Model
 */
fun AuditCourse.convertToCourse(): Course {
    val infoSample = infos[0] // 一个时间段都没有蹭个毛线啊
    val week = Week(start = infoSample.startWeek, end = infoSample.endWeek)
    val arrangeList = mutableListOf<Arrange>()
    infos.forEach {
        val weekType = when (it.weekType) {
            1 -> "单周"
            2 -> "双周"
            3 -> "单双周"
            else -> "这是什么玩意？？？？"
        }
        val arrange = Arrange(
                room = "${it.building}楼${it.room}",
                start = (it.startTime - 1) * 2 + 1, // 因为蹭课API的返回节数 是算的大节 但是课程表Course算的是小节 为了复用我们要转换一下 Fuck
                end = (it.startTime - 1) * 2  + it.courseLength,
                day = it.weekDay,
                week = weekType
        )
        arrangeList.add(arrange)
    }
    val course = Course(
            coursetype = "蹭课",
            college = college,
            ext = "老夫就是牛逼，要蹭课咋地",
            teacher = infoSample.teacher,
            week = week,
            coursename = courseName,
            arrangeBackup = arrangeList,
            campus = "emmmm自己猜好了",
            coursenature = "蹭课",
            credit = "学分有价，蹭课精神无价"
    )
    return course
}

class AuditInfoItemTypeConverter {
    val gson: Gson = Gson()
    val type: Type = object : TypeToken<List<InfoItem>>() {}.type

    @TypeConverter
    fun stringToWeek(json: String): List<InfoItem> {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun weekToString(infos: List<InfoItem>): String {
        return gson.toJson(infos, type)
    }

}
