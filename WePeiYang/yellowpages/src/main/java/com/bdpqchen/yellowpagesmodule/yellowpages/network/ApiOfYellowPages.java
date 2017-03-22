package com.bdpqchen.yellowpagesmodule.yellowpages.network;

import com.bdpqchen.yellowpagesmodule.yellowpages.model.DataBean;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.DatabaseVersion;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Feedback;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chen on 17-2-26.
 */

public interface ApiOfYellowPages {

    @GET("data2")
//    @GET("test_final2")
    Observable<DataBean> getDataList();

    @GET("version")
    Observable<DatabaseVersion> getDbVersion();

    @GET("feedback")
    Observable<Feedback> submit(@Query("type") String type,
                                @Query("phone") String phone,
                                @Query("name") String name);
}
