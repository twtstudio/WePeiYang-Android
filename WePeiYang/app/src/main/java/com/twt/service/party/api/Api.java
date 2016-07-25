package com.twt.service.party.api;

import com.twt.service.party.bean.Status;
import com.twt.service.party.bean.UserInfomation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dell on 2016/7/19.
 */
public interface Api {


    @GET("auth/self/")
    Call<UserInfomation> getInfomation(@Query("token") String token);

    @GET("party/")
    Call<Status> getStatus(@Query("page") String page, @Query("do") String doWhat, @Query("sno") String sno);
}
