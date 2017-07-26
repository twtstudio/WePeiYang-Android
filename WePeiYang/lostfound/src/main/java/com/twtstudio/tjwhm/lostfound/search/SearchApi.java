package com.twtstudio.tjwhm.lostfound.search;

import com.twtstudio.tjwhm.lostfound.waterfall.WaterfallBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public interface SearchApi {
    @GET("lostfound/search")
    Observable<WaterfallBean> loadSearchData(@Query("keyword") String keyword, @Query("page") String page);
}
