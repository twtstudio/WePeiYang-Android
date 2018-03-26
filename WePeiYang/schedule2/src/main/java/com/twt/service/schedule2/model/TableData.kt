package com.twt.service.schedule2.model

import com.google.gson.annotations.SerializedName
import com.twt.service.schedule2.extensions.even
import kotlin.math.absoluteValue

data class Course(val coursetype: String = "",
                  val college: String = "",
                  val ext: String = "",
                  val classid: String = "",
                  val teacher: String = "",
                  val week: Week,
                  val coursename: String = "",
                  val arrange: List<Arrange>,
                  val campus: String = "",
                  val coursenature: String = "",
                  val credit: String = "",
                  val courseid: String = "",
                  val type: CourseType = CourseType.SINGLE,
                  var available: Boolean = false,
                  val next: List<Course>? = null) {

    /**
     * 检查是不是这周有课
     */
    fun isWeekAvailable(weekInt: Int): Boolean {
        if (weekInt < week.start || weekInt > week.end) {
            return false
        }

        if (arrange.any { it.week == "单周" } && !weekInt.even) return true

        if (arrange.any { it.week == "双周" } && weekInt.even) return true

        if (arrange.any { it.week == "单双周" }) return true

        return false
    }

    /**
     * 是否有 星期几的课程
     */
    fun isContainDay(dayInt: Int) = arrange.any { it.day == dayInt }

}


data class Classtable(val week: Int = 0,
                      @SerializedName("data") val courses: List<Course>,
                      val termStart: Long = 0,
                      val updatedAt: String = "",
                      val term: String = "")


data class Arrange(val week: String = "",
                   val start: Int = 0,
                   val end: Int = 0,
                   val day: Int = 0,
                   val room: String = "")

data class Week(val start: Int = 0,
                val end: Int = 0)

enum class CourseType {
    SINGLE, MULTIPLE
}
