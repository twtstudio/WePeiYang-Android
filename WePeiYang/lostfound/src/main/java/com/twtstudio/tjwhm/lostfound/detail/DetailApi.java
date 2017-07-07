package com.twtstudio.tjwhm.lostfound.detail;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by tjwhm on 2017/7/7.
 **/

public interface DetailApi {
    @GET("lostfound/{id}")
    Observable<DetailBean> loadDetailData(@Path("id") String id);
}
