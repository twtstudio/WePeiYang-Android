package com.twt.service.schedule2.model

import android.arch.persistence.room.Ignore
import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.twt.service.schedule2.extensions.even
import java.lang.reflect.Type
import java.util.*
import kotlin.math.absoluteValue

data class Course(val coursetype: String = "",
                  val college: String = "",
                  val ext: String = "",
                  val classid: Int = 0,
                  val teacher: String = "",
                  val week: Week,
                  val coursename: String = "",
                  @SerializedName("arrange") val arrangeBackup: List<Arrange>,
                  val campus: String = "",
                  val coursenature: String = "",
                  val credit: String = "",
                  val courseid: String = "",
                  var courseColor: Int = 0,
                  var weekAvailable: Boolean = false, // 是不是灰色
                  var dayAvailable: Boolean = false) // 今天有没有课
{

    private var nextList: MutableList<Course>? = null
    val next: MutableList<Course>
        get() = if (nextList != null) nextList!! else {
            nextList = LinkedList()
            nextList!!
        }

    var realArrange: MutableList<Arrange>? = null
    val arrange: MutableList<Arrange>
        get() {
            if (realArrange == null) {
                refresh()
            }
            return realArrange!!
        }

    /**
     * 重置课程状态 因为有些东西是Mutable的 就很蛋疼
     */
    fun refresh() {
        weekAvailable = false
        dayAvailable = false
        next.removeAll { true }
        if (realArrange == null) {
            realArrange = mutableListOf()
        }
        realArrange?.apply {
            removeAll { true }
            addAll(arrangeBackup)
        }
    }

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
                      @SerializedName("term_start") val termStart: Long = 0L,
                      @SerializedName("updated_at") val updatedAt: String = "",
                      val term: String = "")


data class Arrange (val week: String = "",/*单双周 单周 双周*/
                   val start: Int = 0,/*第几节开始*/
                   val end: Int = 0,/*第几节结束*/
                   val day: Int = 0,/*周几*/
                   val room: String = ""/*上课地点*/)

data class Week (val start: Int = 0,
                 val end: Int = 0)

enum class CourseType {
    SINGLE, MULTIPLE
}

class ArrangeListTypeConverter {
    val gson: Gson = Gson()
    val type: Type = object : TypeToken<List<Arrange>>() {}.type

    @TypeConverter
    fun stringToArranleList(json: String): List<Arrange> {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun arrangeListToString(arrangeList: List<Arrange>): String {
        return gson.toJson(arrangeList, type)
    }

}

class WeekTypeConverter {
    val gson: Gson = Gson()
    val type: Type = object : TypeToken<Week>() {}.type

    @TypeConverter
    fun stringToWeek(json: String): Week {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun weekToString(week: Week): String {
        return gson.toJson(week, type)
    }

}