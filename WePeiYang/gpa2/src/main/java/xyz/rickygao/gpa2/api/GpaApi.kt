package xyz.rickygao.gpa2.api

import retrofit2.http.GET
import rx.Observable


/**
 * Created by rickygao on 2017/11/9.
 */
interface GpaApi {
    @GET("gpa")
    fun getGpa(): Observable<Response<GpaBean>>
}