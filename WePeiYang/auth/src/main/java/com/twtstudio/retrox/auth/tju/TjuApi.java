package com.twtstudio.retrox.auth.tju;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by retrox on 01/03/2017.
 */

public interface TjuApi {

    @GET("/auth/bind/tju")
    Observable<ResponseBody> bindTju(@Query("tjuuname") String tjuuname,@Query("tjupasswd") String tjupasswd);

}
