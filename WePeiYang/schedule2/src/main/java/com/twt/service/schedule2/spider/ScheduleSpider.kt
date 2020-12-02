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
import java.text.SimpleDateFormat
import java.util.*

object ScheduleSpider {
    private const val semesterId = "semester.id"
    private const val ids = "ids"

    fun getScheduleAsync(): Deferred<String> = GlobalScope.async(IO + QuietCoroutineExceptionHandler) {

        val okHttpClient = SpiderCookieManager.getClientBuilder().build()

        val request1 = Request.Builder()
                .url("http://classes.tju.edu.cn/eams/courseTableForStd.action")
                .get()
                .build()

        val html1 = okHttpClient.newCall(request1).execute().body()?.string().orEmpty()

        val map = if (checkMinor(html1)) {
            //有辅修 再发一个
            val request2 = Request.Builder()
                    .url("http://classes.tju.edu.cn/eams/courseTableForStd!innerIndex.action?projectId=1&_=${System.currentTimeMillis()}")
                    .get()
                    .build()

            val html2 = okHttpClient.newCall(request2).execute().body()?.string().orEmpty()
            parseHtml(html2)
        } else {
            //一般情况
            parseHtml(html1)
        }

        val request0 = Request.Builder()
                .url("http://classes.tju.edu.cn/eams/courseTableForStd!courseTable.action")
                .post(MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("ignoreHead", "1")
                        .addFormDataPart("setting.kind", "std")
                        .addFormDataPart("startWeek", "")
                        .addFormDataPart("semester.id", map[semesterId] ?: "47")//找不到规律
                        .addFormDataPart("ids", map[ids] ?: "0000000")//用户自己的id
                        .build())
                .build()

        okHttpClient.newCall(request0).execute().body()?.string().orEmpty()
    }

    //辅修情况
    private fun checkMinor(html1: String): Boolean = Jsoup.parse(html1).body().getElementsByTag("script").first().data().contains("bg.ready")

    //拿出有用的request里的两个参数
    private fun parseHtml(html: String): MutableMap<String, String> {

        val doc = Jsoup.parse(html)
        val semesterInput = doc.body().getElementsByTag("script")[2].data()
        val semesterIndex = semesterInput.indexOf("value:")
        val semesterId = semesterInput.substring(semesterIndex + 7, semesterIndex + 9)

        val script = doc.getElementsByAttributeValue("language", "JavaScript").first().data()
        val idsIndex = script.indexOf("\"ids\",")
        val ids = script.substring(idsIndex + 7, idsIndex + 14)
//        Log.d("parseHtml", "$semesterId,$ids")

        return mutableMapOf(this.semesterId to semesterId, this.ids to ids)
    }

    //解析课表信息
    fun String.parseHtml(): Classtable? {
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

//                val arrange1List = mutableListOf<Arrange>()
                val weeks = tds[6].text().split("-")
                val classId = tds[1].text().substring(1).toInt() // 逻辑班号
                val teacher = tds[5].text()
                val week = Week(start = weeks[0].toInt(), end = weeks[1].toInt())
                val campus = tds[9].text().let {
                    if (it.isNotEmpty()) {
                        "${Regex("\\w*校区").find(it)}校区"
                    } else {
                        ""
                    }
                }
                val credit = if (tds[4].text().contains(".")) {
                    tds[4].text()
                } else {
                    tds[4].text() + ".0"
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
                        credit = credit,
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
            val removeList = mutableListOf<Int>()
            for ((classId, course) in courseMap) {
                if (arrangeMap[classId].isNullOrEmpty()) {
                    removeList.add(classId)
                } else {
                    courseMap[classId]!!.arrangeBackup = arrangeMap.getValue(classId)
                }
            }
            for (index in removeList) {
                courseMap.remove(index)
            }
            courses.addAll(courseMap.values)

            return Classtable(
                    week = 1,
                    cache = false,
                    courses = courses,
                    termStart = 1598803200L,//TODO  unix时间戳 2020/8/31 0:0:0
                    updatedAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date(System.currentTimeMillis())),// 2020-03-10T20:07:41+08:00 ?
                    term = "20211"//TODO  只能手动改了马
            )

        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

        return null
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
//            val act = Regex("\"\\w*\"").find(lineList[1])!!.value
//            val actTeacher = act.substring(1, act.length - 1)
            //课程相关那一行
            val courseLine = lineList[14].split(",")
            //主键 classId
            //希望不会为空emm
            val firstIndex = Regex("\\(").find(courseLine[4])!!.range.first
            val lastIndex = Regex("\\)").find(courseLine[4])!!.range.last
            val classId = courseLine[4].substring(firstIndex + 1, lastIndex).toInt()
            //上课教室
            val room = if (courseLine[7].isBlank()) {
                ""
            } else {
                courseLine[7].substring(1, courseLine[7].length - 1)
            }
            //周时间
            val weekOri = courseLine[8]
            val finder = Regex("1").findAll(weekOri)
            val week = when {
                Regex("11").containsMatchIn(weekOri) -> "单双周"
                finder.first().range.first % 2 == 1 -> "双周"
                else -> "单周"
            }
//            val weekStart = finder.first().range.first - 1
//            val weekEnd = finder.last().range.last - 1
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
//                Log.d("arrange1", arrangeList.toString())
            } else {
                courseArrangeMap[classId]?.deduplicateAdd(arrange)
//                Log.d("arrange1", arrangeList.toString())
            }

/*            if (courseArrange2Map[classId].isNullOrEmpty()) {
                courseArrange2Map[classId] = arrange2List
            } else {
                courseArrange2Map[classId]?.addAll(arrange2List)
            }*/
        }
        return courseArrangeMap
    }

    //arrange不重复添加
    private fun MutableList<Arrange>.deduplicateAdd(arrange: Arrange) {
        var flag = true//可以添加
        forEach {
            flag = flag && !it.sameArrange(arrange)
        }
        if (flag) {
            add(arrange)
        }
    }

    private fun Arrange.sameArrange(arrange: Arrange): Boolean {
        return arrange.day == day && arrange.end == end && /*arrange.room == room && */arrange.start == start && arrange.week == week
    }
}