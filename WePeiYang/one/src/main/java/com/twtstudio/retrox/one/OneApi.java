package com.twtstudio.retrox.one;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by retrox on 2017/2/11.
 */

public interface OneApi {
    @GET("idlist/0")
    Observable<IdList> getOneIdList();

    @GET("detail/{id}")
    Observable<OneDetailBean> getOneDetail(@Path("id") String id);

}
