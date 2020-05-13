package com.twt.service.schedule2.spider

import android.util.Log
import com.twt.service.schedule2.model.*
import xyz.rickygao.gpa2.spider.utils.*
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.MultipartBody
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object ScheduleSpider {

    fun getScheduleAsync(): Deferred<String> = GlobalScope.async(IO + QuietCoroutineExceptionHandler) {

        val okHttpClient = SpiderTjuApi.getClientBuilder().build()
        val request = Request.Builder()
                .url("http://classes.tju.edu.cn/eams/courseTableForStd!courseTable.action")
                .post(MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("ignoreHead", "1")
                        .addFormDataPart("setting.kind", "std")
                        .addFormDataPart("startWeek", "")
                        .addFormDataPart("semester.id", "31")//TODO  咋个获取
                        .addFormDataPart("ids", "2143898")//TODO  ?
                        .build())
                .build()

        okHttpClient.newCall(request).execute().body()?.string().orEmpty()
    }

    fun String.parseHtml(): Classtable {
        val courses = mutableListOf<Course>()
        try {
            val doc = Jsoup.parse(this)
            //获得tableList里的第二个tbody里的所有课程
            val trs = doc.getElementsByClass("gridtable")[1]
                    .select("tbody").select("tr")
            //获得部分js代码
            val script = doc.getElementsByAttributeValue("language", "JavaScript")[2].data()

            //html
            val courseMap = mutableMapOf<Int, Course>()
            for (tr: Element in trs) {
                //一节课
                val tds = tr.select("td")

//            val arrange1List = mutableListOf<Arrange>()
                val weeks = tds[6].text().split("-")
                val classId = tds[1].text().substring(1).toInt() // 逻辑班号
                val teacher = tds[5].text()
                val week = Week(start = weeks[0].toInt(), end = weeks[1].toInt())
                val campus = tds[9].text().let {
                    if (it.isNotEmpty()) {
                        it.substring(0, 5)
                    } else {
                        ""
                    }
                }

                val course = Course(
                        coursetype = "",//“专业核心”  原数据也没有返回
                        college = "",//啥啊这是  事实上原数据也没有返回
                        ext = "",//“重修”?哪里看啊  原数据也没有返回
                        classid = classId,
                        teacher = teacher,
                        week = week,
                        coursename = tds[3].text(),
                        arrangeBackup = mutableListOf(),
                        campus = campus,
                        coursenature = "",//“必修”，但事实上原数据也没有返回
                        credit = tds[4].text(),
                        courseid = tds[2].text(), // 课程编号
                        courseColor = 0,
                        statusMessage = "", // [蹭课] [非本周] 什么的
                        weekAvailable = false, // 是不是灰色
                        dayAvailable = false // 今天有没有课
                )
                courseMap[classId] = course
            }

            //根据js代码的处理
            val arrangeMap = parseJS(script)
            for ((classId, course) in courseMap) {
                if (arrangeMap[classId].isNullOrEmpty()) {
                    courseMap.remove(classId)//暂时这样处理吧emm
                } else {
                    courseMap[classId]!!.arrangeBackup = arrangeMap.getValue(classId)//应该没事了就emm
                }
            }

            courses.addAll(courseMap.values)

        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } finally {
            return Classtable(
                    week = 1,
                    cache = false,
                    courses = courses,
                    termStart = 1581868800L,//TODO  啥意思
                    updatedAt = "2020-03-10T20:07:41+08:00",//TODO  2020-03-10T20:07:41+08:00
                    term = "19202"//TODO  19202
            )
        }
    }

    //手动解析js
    private fun parseJS(script: String): Map<Int, List<Arrange>> {


        val courseArrangeMap = mutableMapOf<Int, MutableList<Arrange>>()

//        val arrange2List = mutableListOf<Arrange2>()
//        val courseArrange2Map = mutableMapOf<Int, MutableList<Arrange2>>()
        val courseList = script.split("var teachers =")

        for (i in 1 until courseList.size) {
            //一节课
            val lineList = courseList[i].split(";")
            //上课老师
            val act = Regex("\"\\w*\"").find(lineList[1])!!.value
            val actTeacher = act.substring(1, act.length - 1)
            //课程相关那一行
            val courseLine = lineList[14].split(",")
            //主键
            //希望不会为空emm
            val classId = courseLine[4].substring(7, 12).toInt()
            val room = courseLine[7].substring(1, courseLine[7].length - 1)
            //周时间
            val weekOri = courseLine[8]
            val finder = Regex("1").findAll(weekOri)
            val week = when {
                Regex("11").containsMatchIn(weekOri) -> "单双周"
                finder.first().range.first % 2 == 1 -> "双周"
                else -> "单周"
            }
            val weekStart = finder.first().range.first - 1
            val weekEnd = finder.last().range.last - 1
            //课时间
            val day = Regex("(\\d+)").find(lineList[15])?.value!!.toInt() + 1
            val start = Regex("(\\d+)").findAll(lineList[15]).elementAt(1).value.toInt() + 1
            var end = start
            for (j in 15 until lineList.size step 2) {
                if (Regex("=\\w\\*unitCount\\+\\w").containsMatchIn(lineList[j])) {
                    end = Regex("(\\d+)").findAll(lineList[j]).elementAt(1).value.toInt() + 1
                }
            }
            val arrangeList = mutableListOf<Arrange>()
            val arrange = Arrange(
                    week = week,
                    start = start,
                    end = end,
                    day = day,
                    room = room
            )
            arrangeList.add(arrange)

/*            val arrange2 = Arrange2(
                    week = week,
                    courseWeek = Week(
                            start = weekStart,
                            end = weekEnd
                    ),
                    actTeacher = actTeacher,
                    start = start,
                    end = end,
                    day = day,
                    room = room
            )
            arrange2List.add(arrange2)*/

            //整合进map里
            if (courseArrangeMap[classId].isNullOrEmpty()) {
                courseArrangeMap[classId] = arrangeList
                Log.d("arrange1", arrangeList.toString())
            } else {
                courseArrangeMap[classId]?.addAll(arrangeList)
                Log.d("arrange1", arrangeList.toString())
            }

/*            if (courseArrange2Map[classId].isNullOrEmpty()) {
                courseArrange2Map[classId] = arrange2List
            } else {
                courseArrange2Map[classId]?.addAll(arrange2List)
            }*/
        }
        return courseArrangeMap
    }
}