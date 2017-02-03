package com.twtstudio.retrox.gpa;



import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by retrox on 2017/1/26.
 */

public interface GpaApi {
    @GET("gpa")
    Observable<MyGpaBean> getGpa();
}
