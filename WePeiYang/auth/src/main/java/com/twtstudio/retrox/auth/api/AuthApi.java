package com.twtstudio.retrox.auth.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

/**
 * Created by retrox on 2017/1/31.
 */

public interface AuthApi {

    @GET("auth/token/get")
    Single<Response<Token>> login(@Query("twtuname") String twtuname, @Query("twtpasswd") String twtpasswd);

    @GET("http://open.twtstudio.com/api/v2/auth/self")
    Single<AuthSelfBean> authSelf();

    @GET("auth/token/refresh")
    Single<Response<Token>> refreshToken();

    @GET("auth/dropout")
    Single<Response<String>> dropOut(@Query("mode") int mode);

}
