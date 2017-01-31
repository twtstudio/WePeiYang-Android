package com.twtstudio.retrox.wepeiyangrd.api;

import com.twt.wepeiyang.commons.network.ApiResponse;
import com.twt.wepeiyang.commons.auth.login.Token;
import com.twtstudio.retrox.wepeiyangrd.home.common.oneItem.OneInfoBean;

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
