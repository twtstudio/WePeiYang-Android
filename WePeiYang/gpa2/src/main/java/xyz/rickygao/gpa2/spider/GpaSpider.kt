package xyz.rickygao.gpa2.spider

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import xyz.rickygao.gpa2.service.*
import xyz.rickygao.gpa2.spider.cookie.OkHttpClientGenerator
import xyz.rickygao.gpa2.spider.utils.Classes

class GpaSpider {
    lateinit var gpaBean: GpaBean
    var termList: MutableList<Term> = ArrayList()
    var currentTermStr: String = ""
    var courseList: MutableList<Course> = ArrayList()


    private fun login(tjuUName: String, tjuPassword: String) {
        val classes = Classes()
        classes.init()
        classes.login(tjuUName, tjuPassword)
    }

    fun getGpa(tjuUName: String, tjuPassword: String): Deferred<String?> {
        login(tjuUName, tjuPassword)
        val okHttpClient = OkHttpClientGenerator.generate().build()
        val request = Request.Builder()
                .url("http://classes.tju.edu.cn/eams/teach/grade/course/person!historyCourseGrade.action?projectType=MAJOR")
                .get()
                .build()
        return GlobalScope.async {
            okHttpClient.newCall(request).execute().body()?.string()
        }
    }


    fun parseHtml(tjuUName: String, tjuPassword: String, semesterId: String) {
        val htmlStr: String = getGpa(tjuUName, tjuPassword).toString()

        val doc = Jsoup.parse(htmlStr)
        // 根据id获取table
        val table: Elements = doc.getElementsByClass("gridtable")


        // 使用选择器选择该table内所有的<tr> <tr/>
        val totalTrs: Elements = table[0].select("tbody").select("tr")
        val totalThs: Elements = totalTrs[0].select("th")

        val totalCredits = totalThs[2].text().toDouble()
        val totalGpa = totalThs[3].text().toDouble()
        val totalScore = totalThs[4].text().toDouble()

        val total = Total(totalCredits, totalGpa, totalScore)

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

                val type = tds[3].text()
                var typeNum = 1
                if (type == "必修") {
                    typeNum = 0
                }
                val credits = tds[5].text().toDouble()
                val score = tds[6].text().toDouble()

                val course = Course(no, name, typeNum, credits, 0, score, null)

                courseList.add(course)

            } else if (currentTermStr != term) {
                //TODO: 这里要在本地计算一下本学期的加权绩点和学分,存在currentTermStat里
                //存一下单个学期的信息，然后清空list存下一个学期的
                val currentTermStat = TermStat(0.0, 0.0, 0.0)
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
                val score = tds[6].text().toDouble()

                val course = Course(no, name, typeNum, credits, 0, score, null)

                courseList.add(course)
            }

        }

        gpaBean = GpaBean(stat, termList, "", "")
    }

}