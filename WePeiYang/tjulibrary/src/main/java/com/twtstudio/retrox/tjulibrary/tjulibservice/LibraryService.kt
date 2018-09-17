package com.twtstudio.retrox.tjulibrary.tjulibservice

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import com.twtstudio.retrox.tjulibrary.provider.Info
import com.twtstudio.retrox.tjulibrary.provider.RenewResult
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface LibraryApi {
    @GET("v1/library/book/borrowRank/getBookImg/{id}")
    fun getImg(@Path("id") id: Int): Deferred<Img>

    @GET("v1/library/book/borrowRank/{time}")
    fun getRank(@Path("time") time: Int): Deferred<List<RankList>>

    @GET("v1/library/book/?title={key}&page={page}")
    fun getSearch(@Path("key") key: String, @Path("page") page: Int): Deferred<SearchData>

    @GET("v1/library/book/getTotalBorrow/{id}")
    fun getTotalNum(@Path("id") id: Int): Deferred<TotalNum>

    @GET("v1/library/book/{index}")
    fun getBook(@Path("index") index: Int): Deferred<Book>

    @GET("v1/library/user/info")
    fun getUser(): Deferred<CommonBody<Info>>

    @GET(" /book/getISBN/{id}")
    fun getISBN(@Path("id") id: Int): Deferred<IsbnNumber>

    @GET("v1/library/renew/{barcode}")
    fun renewBook(@Path("barcode") barcode: String): Deferred<CommonBody<List<RenewResult>>>


    companion object : LibraryApi by ServiceFactory()
}

interface DoubanApi {
    @GET("v2/book/isbn/{id}")
    fun getBookContent(@Path ("id") id : Int): Deferred<BookContent>

    companion object : DoubanApi by DoubanFactory()
}

data class Img(
        val id: Int,
        val img_url: String
)


data class RankList(
        val bookID: String,
        val bookName: String,
        val borrowNum: String,
        val publisher: String
)


data class SearchData(
        val error_code: Int,
        val message: String,
        val data: List<Data>
)

data class Data(

        val index: String,
        val title: String,
        val author: String,
        val publisher: String,
        val isbn: String,
        val year: String,
        val cover: String
)

data class TotalNum(
        val bookID: String,
        val totalBorrowNum: Int
)

data class Book(
        val error_code: Int,
        val message: String,
        val data: Datax
)

data class Datax(
        val id: String,
        val title: String,
        val authorPrimary: List<String>,
        val authorSecondary: List<Any>,
        val publisher: String,
        val place: String,
        val year: String,
        val topic: List<String>,
        val cover: String,
        val holding: List<Holding>
)

data class Holding(
        val id: Int,
        val barcode: String,
        val callno: String,
        val stateCode: Int,
        val state: String,
        val libCode: String,
        val lib: String,
        val localCode: String,
        val local: String,
        val typeCode: String,
        val type: String,
        val indate: String,
        val loan: Any
)

data class IsbnNumber(
        val id: String,
        val isbn: String
)

data class RenewResult(
        var barcode: String,
        var error: Int,
        var message: String
)

data class BookContent(
    val rating: Rating,
    val subtitle: String,
    val author: List<String>,
    val pubdate: String,
    val tags: List<Any>,
    val origin_title: String,
    val image: String,
    val binding: String,
    val translator: List<Any>,
    val catalog: String,
    val pages: String,
    val images: Images,
    val alt: String,
    val id: String,
    val publisher: String,
    val isbn10: String,
    val isbn13: String,
    val title: String,
    val url: String,
    val alt_title: String,
    val author_intro: String,
    val summary: String,
    val price: String
)

data class Rating(
    val max: Int,
    val numRaters: Int,
    val average: String,
    val min: Int
)

data class Images(
    val small: String,
    val large: String,
    val medium: String
)
