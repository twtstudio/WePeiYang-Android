package com.twtstudio.tjwhm.lostfound.waterfall;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tjwhm on 2017/7/5.
 **/

public interface WaterfallApi {
    @GET("lostfound/{lostOrFound}")
    Observable<WaterfallBean> loadWaterData
            (@Path("lostOrFound") String lostOrFound,@Query("page") String page);
}
