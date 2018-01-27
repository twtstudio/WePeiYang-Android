package com.twtstudio.retrox.auth.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by retrox on 2017/1/31.
 */

public interface AuthApi {

    @GET("auth/token/get")
    Observable<Response<Token>> login(@Query("twtuname") String twtuname, @Query("twtpasswd") String twtpasswd);

    @GET("http://open.twtstudio.com/api/v2/auth/self")
    Observable<AuthSelfBean> getAuthSelf();

    @GET("auth/token/refresh")
    Observable<Response<Token>> refreshToken();

    @GET("auth/dropout")
    Observable<Response<String>> dropOut(@Query("mode") int mode);

}
