package com.twtstudio.retrox.tjulibrary.tjulibservice

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.CoroutineCallAdapterFactory
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import com.twtstudio.retrox.tjulibrary.provider.Info
import com.twtstudio.retrox.tjulibrary.provider.RenewResult
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal const val LIB_IMG_URL = "http://47.95.216.239:5678/"
interface LibraryApi {
    @GET("v1/library/book/getBookImg/{id}")
    fun getImg(@Path("id") id : String) : Deferred<Img>

    @GET("v1/library/book/borrowRank/{time}")
    fun getRank(@Path("time") time : Int) : Deferred<List<RankList>>

    @GET("v1/library/book/?title={key}&page={page}")
    fun getSearch(@Path("key") key : String,@Path("page") page : Int) : Deferred<SearchData>

    @GET("v1/library/book/getTotalBorrow/{id}")
    fun getTotalNum(@Path("id") id: String) : Deferred<TotalNum>

    @GET("v1/library/book/{index}")
    fun getBook(@Path("index") index: String) : Deferred<Book>

    @GET("v1/library/user/info")
    fun getUser() : Deferred<CommonBody<Info>>

    @GET("v1/library/book/getISBN/{id}")
    fun getISBN(@Path("id") id: String): Deferred<IsbnNumber>

    @GET("v1/library/renew/{barcode}")
    fun renewBook(@Path("barcode") barcode: String): Deferred<CommonBody<List<RenewResult>>>
    @GET("${LIB_IMG_URL}getImgs.php")
    fun getImgUrl(@Query ("isbns") isbns : String) : Deferred<List<imgSrc>>


    companion object : LibraryApi by ServiceFactory()
}


data class Img(
        val id: Int,
        val img_url : String
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
    val isbn: String,
    val title: String,
    val price: String,
    val authorPrimary: List<String>,
    val authorSecondary: List<Any>,
    val publisher: String,
    val place: String,
    val year: String,
    val summary: String?,
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
        val tags: List<Tag>,
        val origin_title: String,
        val image: String,
        val binding: String,
        val translator: List<String>,
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

data class Tag(
        val count: Int,
        val name: String,
        val title: String
)

data class Images(
        val small: String,
        val large: String,
        val medium: String
)


data class SearchBook(
        var bookID: String,
        var booktitle : String,
        var bookartist : String,
        var bookpublish : String,
        var number : String
)

data class imgSrc(
    val result: List<Result>
)

data class Result(
    val metaResID: Any,
    val isbn: String,
    val coverlink: String,
    val handleTime: Long,
    val fromRes: Any,
    val status: Int
)



