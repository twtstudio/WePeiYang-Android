package com.twtstudio.retrox.gpa;

import com.twt.wepeiyang.commons.network.ApiResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by retrox on 2017/1/26.
 */

public interface GpaApi {
    @GET("gpa")
    Observable<ApiResponse<GpaBean>> getGpa();
}
