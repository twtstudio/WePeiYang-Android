package com.twt.wepeiyang.commons.network;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by retrox on 2017/1/31.
 */

interface CommonApi {

    @GET("auth/token/refresh")
    Observable<ResponseBody> refreshToken();

}
