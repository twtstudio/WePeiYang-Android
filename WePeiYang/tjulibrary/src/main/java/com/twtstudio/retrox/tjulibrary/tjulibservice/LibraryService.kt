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
    fun getImg(@Path("id") id : Int) : Deferred<Img>

    @GET("v1/library/book/borrowRank/{time}")
    fun getRank(@Path("time") time : Int) : Deferred<List<RankList>>

    @GET("v1/library/book/?title={key}&page={page}")
    fun getSearch(@Path("key") key : String,@Path("page") page : Int) : Deferred<SearchData>

    @GET("v1/library/book/getTotalBorrow/{id}")
    fun getTotalNum(@Path("id") id: Int) : Deferred<TotalNum>

    @GET("v1/library/book/{index}")
    fun getBook(@Path("index") index: Int) : Deferred<Book>

    @GET("v1/library/user/info")
    fun getUser() : Deferred<CommonBody<Info>>

    @GET("v1/library/renew/{barcode}")
    fun renewBook(@Path("barcode") barcode: String): Deferred<CommonBody<List<RenewResult>>>


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

data class RenewResult(
        var barcode: String,
        var error: Int,
        var message: String
)

