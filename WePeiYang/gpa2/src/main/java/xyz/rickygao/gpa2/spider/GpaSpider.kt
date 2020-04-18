package xyz.rickygao.gpa2.spider

import android.util.Log
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import xyz.rickygao.gpa2.service.*
import xyz.rickygao.gpa2.spider.cookie.OkHttpClientGenerator
import xyz.rickygao.gpa2.spider.utils.Classes

object GpaSpider {
    lateinit var gpaBean: GpaBean
    var termList: MutableList<Term> = ArrayList()
    var currentTermStr: String = ""
    var courseList: MutableList<Course> = ArrayList()


    private suspend fun login(tjuUName: String, tjuPassword: String) {

        Classes.login(tjuUName, tjuPassword)
        Log.d("login", Thread.currentThread().toString())
    }


    fun getGpa(tjuUName: String, tjuPassword: String): Deferred<String> = GlobalScope.async(IO +QuietCoroutineExceptionHandler) {

        Log.d("gpa", Thread.currentThread().toString())
        login(tjuUName, tjuPassword)
        val okHttpClient = OkHttpClientGenerator.generate().build()
        val request = Request.Builder()
                .url("http://classes.tju.edu.cn/eams/teach/grade/course/person!historyCourseGrade.action?projectType=MAJOR")
                .get()
                .build()

        okHttpClient.newCall(request).execute().body()?.string().orEmpty()
    }

    fun parseHtml(htmlStr: String): GpaBean {
        Log.d("html", htmlStr)
        val doc = Jsoup.parse(htmlStr)
        // 根据id获取table
        val table: Elements = doc.getElementsByClass("gridtable")


        // 使用选择器选择该table内所有的<tr> <tr/>
        val totalTrs: Elements = table[0].select("tbody").select("tr")
        val totalThs: Elements = totalTrs[0].select("th")

        val totalCredits = totalThs[2].text().toDouble()
        val totalGpa = totalThs[3].text().toDouble()
        val totalScore = totalThs[4].text().toDouble()

        val total = Total(totalScore, totalGpa, totalCredits)

        val stat = Stat(null, total)

        val courseTrs: Elements = table[1].select("tbody").select("tr")


        //遍历该表格内的所有的<tr> <tr/>
        for (tr: Element in courseTrs) {
            // 获取一个tr
            // 获取该行的所有td节点
            val tds: Elements = tr.select("td")
            val term = tds[0].text().toString()

            if (currentTermStr == "") {
                currentTermStr = term
            }

            if (currentTermStr == term) {
                val no = tds[1].text()
                val name = tds[2].text()

                val type = tds[4].text()
                var typeNum = 1
                if (type == "必修") {
                    typeNum = 0
                }
                val credits = tds[5].text().toDouble()
                val scoreStr = tds[6].text().toString()
                val score: Double
                if (scoreStr == "P") {
                    score = 100.0
                } else {
                    score = scoreStr.toDouble()
                }
                val gpa = tds[8].text().toDouble()
                val course = Course(no, name, typeNum, credits, 0, score, gpa, null)

                courseList.add(course)

            } else if (currentTermStr != term) {
                var currentTermScore = 0.0
                var currentTermGpa = 0.0
                var currentTermTotalCredits = 0.0
                for (course in courseList) {
                    currentTermTotalCredits += course.credit
                }
                for (course in courseList) {
                    currentTermGpa += course.gpa.times((course.credit / currentTermTotalCredits))
                    currentTermScore += course.score.times((course.credit) / currentTermTotalCredits)
                }
                val currentTermStat = TermStat(currentTermScore, currentTermGpa, currentTermTotalCredits)
                val currentTerm = Term(currentTermStr, courseList, "", currentTermStat)

                termList.add(currentTerm)

                courseList.clear()
                currentTermStr = term

                val no = tds[1].text()
                val name = tds[2].text()

                val type = tds[3].text()
                var typeNum = 1
                if (type == "必修") {
                    typeNum = 0
                }
                val credits = tds[5].text().toDouble()
                val scoreStr = tds[6].text().toString()
                val score: Double
                if (scoreStr == "P") {
                    score = 100.0
                } else {
                    score = scoreStr.toDouble()
                }

                val gpa = tds[8].text().toDouble()
                val course = Course(no, name, typeNum, credits, 0, score, gpa, null)

                courseList.add(course)
            }
        }
        gpaBean = GpaBean(stat, termList, "", "")
        return gpaBean
    }


//
//    fun parseHtml(tjuUName: String, tjuPassword: String): GpaBean {
//        val htmlStr = login(tjuUName, tjuPassword)
//        val doc = Jsoup.parse(htmlStr)
//        // 根据id获取table
//        val table: Elements = doc.getElementsByClass("gridtable")
//
//
//        // 使用选择器选择该table内所有的<tr> <tr/>
//        val totalTrs: Elements = table[0].select("tbody").select("tr")
//        val totalThs: Elements = totalTrs[0].select("th")
//
//        val totalCredits = totalThs[2].text().toDouble()
//        val totalGpa = totalThs[3].text().toDouble()
//        val totalScore = totalThs[4].text().toDouble()
//
//        val total = Total(totalScore, totalGpa, totalCredits)
//
//        val stat = Stat(null, total)
//
//        val courseTrs: Elements = table[1].select("tbody").select("tr")
//
//
//        //遍历该表格内的所有的<tr> <tr/>
//        for (tr: Element in courseTrs) {
//            // 获取一个tr
//            // 获取该行的所有td节点
//            val tds: Elements = tr.select("td")
//            val term = tds[0].text().toString()
//
//            if (currentTermStr == "") {
//                currentTermStr = term
//            }
//
//            if (currentTermStr == term) {
//                val no = tds[1].text()
//                val name = tds[2].text()
//
//                val type = tds[4].text()
//                var typeNum = 1
//                if (type == "必修") {
//                    typeNum = 0
//                }
//                val credits = tds[5].text().toDouble()
//                val scoreStr = tds[6].text().toString()
//                val score: Double
//                if (scoreStr == "P") {
//                    score = 100.0
//                } else {
//                    score = scoreStr.toDouble()
//                }
//                val gpa = tds[8].text().toDouble()
//                val course = Course(no, name, typeNum, credits, 0, score, null, gpa)
//
//                courseList.add(course)
//
//            } else if (currentTermStr != term) {
//                var currentTermScore: Double = 0.0
//                var currentTermGpa: Double = 0.0
//                var totalCredits: Double = 0.0
//                for (course in courseList) {
//                    totalCredits += course.credit
//                }
//                for (course in courseList) {
//                    currentTermGpa += course.gpa?.times((course.credit / totalCredits))!!
//                    currentTermScore += course.score.times((course.credit)/totalCredits)
//                }
//                val currentTermStat = TermStat(currentTermScore, currentTermGpa, totalCredits)
//                val currentTerm = Term(currentTermStr, courseList, "", currentTermStat)
//
//                termList.add(currentTerm)
//
//                courseList.clear()
//                currentTermStr = term
//
//                val no = tds[1].text()
//                val name = tds[2].text()
//
//                val type = tds[3].text()
//                var typeNum = 1
//                if (type == "必修") {
//                    typeNum = 0
//                }
//                val credits = tds[5].text().toDouble()
//                val scoreStr = tds[6].text().toString()
//                val score: Double
//                if (scoreStr == "P") {
//                    score = 100.0
//                } else {
//                    score = scoreStr.toDouble()
//                }
//
//                val gpa = tds[8].text().toDouble()
//                val course = Course(no, name, typeNum, credits, 0, score, null, gpa)
//
//                courseList.add(course)
//            }
//        }
//        gpaBean = GpaBean(stat, termList, "", "")
//        return gpaBean
//    }

}