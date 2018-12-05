package com.yookiely.lostfond2.service

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import okhttp3.MultipartBody
import retrofit2.http.*

internal const val LOST_FOUND_BASE_URL = "https://open-lostfound.twtstudio.com/api/v1/lostfound/"
interface LostFoundService {
    @GET("${LOST_FOUND_BASE_URL}lost")
    fun getLost(@Query("campus") campus: Int, @Query("page") page: Int, @Query("detail_type") detail_type: Int, @Query("timeblock") timeblock: Int): Deferred<CommonBody<List<MyListDataOrSearchBean>>>

    @GET("${LOST_FOUND_BASE_URL}found")
    fun getFound(@Query("campus") campus: Int, @Query("page") page: Int, @Query("detail_type") detail_type: Int, @Query("timeblock") timeblock: Int): Deferred<CommonBody<List<MyListDataOrSearchBean>>>

    @GET("$LOST_FOUND_BASE_URL{id}")
    fun getDetailed(@Path("id") id: Int): Deferred<CommonBody<DetailData>>

    @GET("${LOST_FOUND_BASE_URL}search")
    fun getSearch(@Query("keyword") keyword: String, @Query("campus") campus: Int, @Query("time") time: Int, @Query("page") page: Int): Deferred<CommonBody<List<MyListDataOrSearchBean>>>

    @GET("${LOST_FOUND_BASE_URL}user/{lostOrFound}")
    fun getMyList(@Path("lostOrFound") lostOrFound: String, @Query("page") page: Int): Deferred<CommonBody<List<MyListDataOrSearchBean>>>

    @GET("${LOST_FOUND_BASE_URL}inverse/{id}")
    fun turnStatus(@Path("id") id: String): Deferred<CommonBody<String>>


    @FormUrlEncoded
    @POST("$LOST_FOUND_BASE_URL{lostOrFound}")
    fun updateRelease(@FieldMap map: Map<String, Any>, @Path("lostOrFound") lostOrFound: String): Deferred<CommonBody<List<MyListDataOrSearchBean>>>

    @Multipart
    @POST("$LOST_FOUND_BASE_URL{lostOrFound}")
    fun updateReleaseWithPic(@Path("lostOrFound") lostOrFound: String,
                             @Part partList: List<MultipartBody.Part>): Deferred<CommonBody<List<MyListDataOrSearchBean>>>

    @POST("${LOST_FOUND_BASE_URL}edit/{lostOrFound}/{id}")
    @FormUrlEncoded
    fun updateEdit(@FieldMap map: Map<String, Any>,
                   @Path("lostOrFound") lostOrFound: String,
                   @Path("id") id: String): Deferred<CommonBody<List<MyListDataOrSearchBean>>>

    @POST("${LOST_FOUND_BASE_URL}edit/{lostOrFound}/{id}")
    @Multipart
    fun updateEditWithPic(@Path("lostOrFound") lostOrFound: String,
                          @Path("id") id: String,
                          @Part partList: List<MultipartBody.Part>): Deferred<CommonBody<List<MyListDataOrSearchBean>>>

    @DELETE("$LOST_FOUND_BASE_URL{id}")
    fun delete(@Path("id") id: String): Deferred<CommonBody<String>>

    companion object : LostFoundService by ServiceFactory()


}

data class Data(
        val id: Int,
        val name: String,
        val title: String,
        val place: String,
        val phone: String,
        val isback: Int,
        val picture: String
)

data class DetailData(
        val id: Int,
        val type: Int?,
        val title: String?,
        val name: String?,
        val time: String?,
        val place: String?,
        val phone: String?,
        val item_description: String?,
        val detail_type: Int,
        val picture: List<String>?,
        val card_name: String?,
        val card_number: String?,
        val publish_start: String,
        val publish_end: String,
        val other_tag: String,
        val recapture_place: String?,
        val recapture_entrance: Int?,
        val campus: Int
)

data class InverseID(
        val error_code: Int,
        val message: String,
        val data: String
)

data class MyListDataOrSearchBean(
        val id: Int,
        val type: Int,
        val name: String,
        val title: String,
        val place: String,
        val time: String,
        val phone: String,
        val detail_type: Int,
        val isback: Int,
        val picture: List<String>?,
        val recapture_place: String?,
        val recapture_entrance: Int?,
        val campus: Int,
        val isExpired: Int
)