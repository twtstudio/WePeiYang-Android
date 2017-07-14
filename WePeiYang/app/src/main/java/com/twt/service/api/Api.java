package com.twt.service.api;


import com.twt.service.home.common.oneItem.OneInfoBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by retrox on 2016/11/25.
 */

public interface Api {

    @GET("http://rest.wufazhuce.com/OneForWeb/one/getHpinfo")
    Observable<OneInfoBean> getOneHpInfo(@Query("strDate") String date);
}
