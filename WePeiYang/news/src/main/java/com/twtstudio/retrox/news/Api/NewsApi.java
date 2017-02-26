package com.twtstudio.retrox.news.api;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by retrox on 26/02/2017.
 */

public interface NewsApi {

    @GET("app/index")
    Observable<HomeNewsBean> getHomeNews();

}
