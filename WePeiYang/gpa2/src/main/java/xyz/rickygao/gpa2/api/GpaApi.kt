package xyz.rickygao.gpa2.api

import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import rx.Single


/**
 * Created by rickygao on 2017/11/9.
 */
interface GpaApi {
    @GET("gpa")
    fun get(): Single<Response<GpaBean>>

    @FormUrlEncoded
    @POST("gpa/evaluate")
    fun evaluate(@FieldMap params: Map<String, String>): Single<Response<String>>
}
