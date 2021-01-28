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
import xyz.rickygao.gpa2.spider.utils.SpiderCookieManager
import java.lang.Exception


object GpaSpider {
    private const val DELAYED = 999.0
    private const val IGNORED = 999.0
    private val termNameList: List<String> = listOf("大一上", "大一下", "大二上", "大二下", "大三上", "大三下", "大四上", "大四下", "大五上", "大五下")

    lateinit var gpaBean: GpaBean
    var currentTermStr: String = ""
    var termList: MutableList<Term> = ArrayList()
    var courseList: MutableList<Course> = ArrayList()


    fun getGpa(): Deferred<String> = GlobalScope.async(IO + QuietCoroutineExceptionHandler) {
        clearLocalCache()
        Log.d("gpa", Thread.currentThread().toString())
        val okHttpClient = SpiderCookieManager.getClientBuilder().build()
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

        //计算总学分，总加权和总绩点
        val totalCredits = totalThs[2].text().toDouble()
        val totalGpa = totalThs[3].text().toDouble()
        val totalScore = totalThs[4].text().toDouble()

        val total = Total(totalScore, totalGpa, totalCredits)

        val stat = Stat(null, total)

        val courseTrs: Elements = table[1].select("tbody").select("tr")

        //遍历该表格内的所有的<tr> <tr/>
        for ((index, tr: Element) in courseTrs.withIndex()) {
            // 获取一个tr
            // 获取该行的所有td节点
            val tds: Elements = tr.select("td")
            val term = tds[0].text().toString()

            if (currentTermStr == "") {
                currentTermStr = term
            }

            if (currentTermStr == term) {
                parseCourses(tds)

            } else {
                //计算上学期的数据
                calculateScore()
                //更新当前学期
                currentTermStr = term
                parseCourses(tds)
            }

            //计算最后一学期的数据
            if (index == courseTrs.size - 1) {
                calculateScore()
            }
        }

        for ((index, term) in termList.withIndex()) {
            term.name = termNameList[index]
        }

        gpaBean = GpaBean(stat, termList, "", "")
        return gpaBean
    }

    private fun parseCourses(tds: Elements) {
        val no = tds[1].text()
        val name = tds[2].text()

        val typeNum = when (val type = tds[4].text()) {
            "必修" -> {
                0
            }
            else -> {
                1
            }
        }

        var scoreStr = ""

        try{
            scoreStr = tds[6].text().toString()
        } catch (e: Exception) {
            Log.d("score", "有没评教的")
        }

        val score: Double = when (scoreStr) {
            "P" -> {
                100.0
            }
            "--" -> {
                DELAYED
            }
            "F" -> {
                0.0
            }
            "A" -> {
                IGNORED
            }
            "B" -> {
                IGNORED
            }
            "C" -> {
                IGNORED
            }
            "D" -> {
                IGNORED
            }
            "E" -> {
                IGNORED
            }
            "" -> {
                IGNORED
            }
            else -> {
                scoreStr.toDouble()
            }
        }

        val credits = if (score < 60) {
            0.0
        } else {
            tds[5].text().toDouble()
        }
//        if(score != DELAYED && score != IGNORED) {
//            val gpa = tds[8].text().toDouble()
//        }

        if (score != DELAYED && score != IGNORED) {
            val gpa = tds[8].text().toDouble()
            val course = Course(no, name, typeNum, credits, 0, score, gpa, null)
            courseList.add(course)
        }
    }

    private fun calculateScore() {
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
        val deepCopyCourseList: MutableList<Course> = ArrayList()
        for (temp in courseList) {
            deepCopyCourseList.add(temp)
        }
        val currentTerm = Term(currentTermStr, deepCopyCourseList, "", currentTermStat)

        termList.add(currentTerm)
        courseList.clear()
    }

    private fun clearLocalCache() {
        termList.clear()
        currentTermStr = ""
        courseList.clear()
    }
}