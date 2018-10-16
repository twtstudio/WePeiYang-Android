package com.twtstudio.retrox.tjulibrary.tjulibservice

import android.app.Activity
import android.util.Log

import kotlinx.coroutines.experimental.async

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*

class JsoupService(bookProvide: BookProvide.setBook) : BookProvide.getBook {
    var bookProvide = bookProvide

    override fun setSearch(booklist: ArrayList<SearchBook>) {
        bookProvide.setSearchBook(booklist)
    }


    override fun getSearch(key: String, page: Int, activity: Activity) {
        var list: ArrayList<SearchBook> = ArrayList()
        val random = Random()
        val rand = random.nextInt()

        async {


            try {

                    Log.d("jiuxiangkankanshuidecuo","发送一次")


                val doc = Jsoup.connect("http://opac.lib.tju.edu.cn/opac/search?q=$key&searchType=standard&isFacet=true&view=standard&rows=10&sortWay=score&sortOrder=desc&hasholding=1&searchWay0=marc&q0=&logical0=AND&page=$page")
                        .header("Cookie", "JSESSIONID=$rand; org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE=zh")
                        .get()


                val bookmessage: Elements = doc.select("div.resultList")

                for (i in bookmessage.select("tr")) {
                    var book = SearchBook("", "", "", "","")
                    val bookname: Element = i
                    val id : String = bookname.select("div.bookmeta").attr("bookrecno")
                    val writer: Element = bookname.select("div").first()
                    val title = writer.select("div").select("div")[1].select("span.bookmetaTitle").select("a").text()
                    val artist = writer.select("div").select("div")[2].select("a").text()
                    val publish = writer.select("div").select("div")[3].select("a").text()
                    val number: String = writer.select("div").select("div")[4].select("span").text()


                    var bookName: String = bookname.text()
                    book.bookID = id
                    book.booktitle = title
                    book.bookartist = artist
                    book.bookpublish = publish

                    book.number = number

                    list.add(book)

                }
                activity.runOnUiThread { setSearch(list) }


            } catch (e: Exception) {
                Log.d("chucuole", e.toString())
            }
        }


    }

}

interface BookProvide {
    interface getBook {
        fun getSearch(key: String, page: Int, activity: Activity)

        fun setSearch(booklist: ArrayList<SearchBook>)
    }

    interface setBook {

        fun setSearchBook(booklist: ArrayList<SearchBook>)
    }


}