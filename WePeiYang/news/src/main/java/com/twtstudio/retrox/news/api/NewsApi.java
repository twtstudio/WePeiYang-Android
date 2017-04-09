package com.twtstudio.retrox.news.api;

import com.twtstudio.retrox.news.detail.News;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by retrox on 26/02/2017.
 */

public interface NewsApi {

    @GET("app/index")
    Observable<HomeNewsBean> getHomeNews();

    @GET("news/type/{type}")
    Observable<CommonNewsBean> getNews(@Path("type") int type, @Query("page") int page);

    @GET("news/{index}")
    Observable<News> getNewsDetail(@Path("index") int index);
}
