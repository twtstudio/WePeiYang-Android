package com.twt.service.api;

import com.twt.service.model.NewsItem;
import com.twt.service.model.Token;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by huangyong on 16/5/18.
 */
public interface WePeiYang {

    @GET("/auth/token/get")
    Observable<ApiResponse<Token>> login(@Query("twtuname") String twtuname, @Query("twtpasswd") String twtpasswd);

    @GET("/auth/bind/tju")
    Observable<ApiResponse> bindTju(@Query("tjuuname") String tjuuname, @Query("tjupasswd") String tjupasswd);

    @GET("/news/type/{type}")
    Observable<ApiResponse<List<NewsItem>>> getNewsList(@Path("type") int type, @Query("page") int page);

}
