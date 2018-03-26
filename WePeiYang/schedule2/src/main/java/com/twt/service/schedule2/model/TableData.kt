package com.twt.service.schedule2.model

import com.google.gson.annotations.SerializedName

data class Course(val coursetype: String = "",
                  val college: String = "",
                  val ext: String = "",
                  val classid: String = "",
                  val teacher: String = "",
                  val week: Week,
                  val coursename: String = "",
                  val arrange: List<Arrange>? = null,
                  val campus: String = "",
                  val coursenature: String = "",
                  val credit: String = "",
                  val courseid: String = "",
                  val type: CourseType = CourseType.SINGLE,
                  val next: List<Course>? = null)


data class Classtable(val week: Int = 0,
                      @SerializedName("data") val courses: List<Course>?,
                      val termStart: Int = 0,
                      val updatedAt: String = "",
                      val term: String = "")


data class Arrange(val week: String = "",
                   val start: String = "",
                   val end: String = "",
                   val day: String = "",
                   val room: String = "")

data class Week(val start: String = "",
                val end: String = "")

enum class CourseType {
    SINGLE, MULTIPLE
}
