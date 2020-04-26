package com.twt.service.schedule2.model

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.twt.service.schedule2.extensions.even
import java.lang.reflect.Type
import java.util.*
import javax.annotation.Nullable

data class Course(val coursetype: String = "",
                  val college: String = "",
                  val ext: String = "",
                  val classid: Int = 0, // 逻辑班号
                  val teacher: String = "",
                  val week: Week,
                  val coursename: String = "",
                  @SerializedName("arrange") val arrangeBackup: List<Arrange>,
                  @Nullable @SerializedName("arrange2") var arrangeBackup2: List<Arrange2> = mutableListOf(),
                  val campus: String = "",
                  val coursenature: String = "",
                  val credit: String = "",
                  val courseid: String = "0", // 课程编号
                  var courseColor: Int = 0,
                  var statusMessage: String? = "", // [蹭课] [非本周] 什么的
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

        val arrangeTemp = if (arrange.size > 1) mutableListOf(arrange[0]) else arrange // 一天多节课的问题 只看第一个

        if (arrangeTemp.any { it.week == "单周" } && !weekInt.even) return true

        if (arrangeTemp.any { it.week == "双周" } && weekInt.even) return true

        if (arrangeTemp.any { it.week == "单双周" }) return true

        return false
    }

    /**
     * 是否有 星期几的课程
     */
    fun isContainDay(dayInt: Int) = arrange.any { it.day == dayInt }

    /**
     * 刷新课程的状态信息
     * 因为之前是在Adapter里面渲染的 就导致了 DiffUtil的问题
     */
    fun refreshStatusMessage() {
        this.statusMessage = "" // 初始化
        var text = ""
        if (!this.weekAvailable) {
            text += "[非本周]"
        }
        if (this.coursetype == "蹭课") {
            text += "[蹭课]"
        }
        if (this.ext == "重修") {
            text += "[光荣重修！]"
        }
        this.statusMessage = text

        /**
         * 渲染Next列表里面的课程信息
         */
        if (this.next.size > 0) {
            this.next.forEach {
                var tempText = ""
                if (!it.weekAvailable) {
                    tempText += "[非本周]"
                } else {
                    tempText += "[冲突]"
                }
                if (it.coursetype == "蹭课") {
                    tempText += "[蹭课]"
                }
                if (it.ext == "重修") {
                    tempText += "[光荣重修！]"
                }
                it.statusMessage = tempText
            }
        }
    }

}


data class Classtable(val week: Int = 0,
                      val cache: Boolean = true,
                      @SerializedName("data") val courses: List<Course>,
                      @SerializedName("term_start") val termStart: Long = 0L,
                      @SerializedName("updated_at") val updatedAt: String = "",
                      val term: String = "")


data class Arrange(val week: String = "",/*单双周 单周 双周*/
                   val start: Int = 0,/*第几节开始*/
                   val end: Int = 0,/*第几节结束*/
                   val day: Int = 0,/*周几*/
                   val room: String = ""/*上课地点*/)

data class Arrange2(val week: String = "",/*单双周 单周 双周*/
                    val courseWeek: Week,/*对应原Course的week*/
                    val actTeacher: String = "",/*对应原Course的teacher 上课老师*/
                    val start: Int = 0,/*第几节开始*/
                    val end: Int = 0,/*第几节结束*/
                    val day: Int = 0,/*周几*/
                    val room: String = ""/*上课地点*/)

data class Week(val start: Int = 0,
                val end: Int = 0)

class ArrangeListTypeConverter {
    val gson: Gson = Gson()
    val type: Type = object : TypeToken<List<Arrange>>() {}.type

    @TypeConverter
    fun stringToArrangeList(json: String): List<Arrange> {
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