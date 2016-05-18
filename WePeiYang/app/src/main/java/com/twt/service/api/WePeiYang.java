package com.twt.service.api;

import com.twt.service.model.Token;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by huangyong on 16/5/18.
 */
public interface WePeiYang {

    @GET("/auth/token/get")
    Observable<APIResponse<Token>> login(@Query("twtuname")String twtuname, @Query("twtpasswd")String twtpasswd);

    @GET("/auth/bind/tju")
    Observable<APIResponse> bindTju(@Query("tjuuname")String tjuuname, @Query("tjupasswd")String tjupasswd);
}
