package xyz.rickygao.gpa2.spider

import android.util.Log
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import xyz.rickygao.gpa2.spider.cookie.OkHttpClientGenerator
import xyz.rickygao.gpa2.spider.utils.Classes

class GpaSpider {
    private fun login(tjuUName: String, tjuPassword: String) {
        val classes = Classes()
        classes.init()
        classes.login(tjuUName, tjuPassword)
    }

    fun getGpa(tjuUName: String, tjuPassword: String, semesterId: String): Deferred<String?> {
        login(tjuUName, tjuPassword)
        val okHttpClient = OkHttpClientGenerator.generate().build()
        val request = Request.Builder()
                .url("http://classes.tju.edu.cn/eams/teach/grade/course/person!search.action?semesterId=$semesterId&_=${System.currentTimeMillis()}")
                .get()
                .build()
        return GlobalScope.async {
            okHttpClient.newCall(request).execute().body()?.string()
        }
    }

    fun parseHtml(tjuUName: String, tjuPassword: String, semesterId: String) {
        val htmlStr: String = getGpa(tjuUName, tjuPassword, semesterId).toString()

        val doc = Jsoup.parse(htmlStr)
        // 根据id获取table
        val table: Elements = doc.getElementsByClass("gridtable")
        val tableBody: Elements = table.select("tbody")


        // 使用选择器选择该table内所有的<tr> <tr/>
        val trs: Elements = tableBody.select("tr")
        //遍历该表格内的所有的<tr> <tr/>
        for (tr: Element in trs) {
            // 获取一个tr
            // 获取该行的所有td节点
            val tds: Elements = tr.select("td")
            // 选择某一个td节点
            for (td: Element in tds) {
                val text: String = td.text()
                Log.d("gpaInfo", text)
            }
        }
    }

    data class GpaBean(
            val semester: String,
            val courseCode: String,
            val courseName: String,
            val courseCredits: Int,
            val courseScore: Int,
            val courseGpa: Double
    )
}