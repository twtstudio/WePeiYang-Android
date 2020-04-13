package com.twt.service.schedule2.spider

import com.twt.service.schedule2.model.Arrange
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.Course
import com.twt.service.schedule2.model.Week
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class ScheduleSpider {

    private fun getSchedule() {
        //http://classes.tju.edu.cn/eams/courseTableForStd!courseTable.action
    }

    fun parseHtml() {
        val courseList = mutableListOf<Course>()

        val htmlStr: String = getSchedule().toString()
        val doc = Jsoup.parse(htmlStr)
        // 获取tableList
        val tables: Elements = doc.getElementsByClass("gridtable")
        //两个tbody
        val tableBody1: Elements = tables[0].select("tbody")
        val tableBody2: Elements = tables[1].select("tbody")

        val arrange = mutableListOf<Arrange>(

        )

        val trs1: Elements = tableBody2.select("tr")
        val trs2: Elements = tableBody2.select("tr")
        for (tr: Element in trs2) {
            val tds: Elements = tr.select("td")
            val week = tds[6].text().split("-")

            courseList.add(Course(
                    coursetype = "",
                    college = "",
                    ext = "",
                    classid = tds[1].text().substring(1, 4).toInt(), // 逻辑班号
                    teacher = tds[5].text(),
                    week = Week(start = week[0].toInt(), end = week[1].toInt()),
                    coursename = tds[3].text(),
                    arrangeBackup = arrange,
                    campus = tds[9].text().substring(0, 4),
                    coursenature = "",
                    credit = tds[4].text(),
                    courseid = tds[2].text(), // 课程编号
                    courseColor = 0,
                    statusMessage = "", // [蹭课] [非本周] 什么的
                    weekAvailable = false, // 是不是灰色
                    dayAvailable = false // 今天有没有课
            ))

        }


        val classTable = Classtable(
                week = 0,
                cache = true,
                courses = courseList,
                termStart = 0L,//TODO
                updatedAt = "",//TODO
                term = ""//TODO
        )
    }
}