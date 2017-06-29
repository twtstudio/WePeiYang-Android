package com.twtstudio.retrox.gpa.client;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by sunjuntao on 15/11/5.
 */
public interface Api {

    static final String AUTHORIZATION = "Authorization";


    @GET("/gpa")
    void getGPA(@Header(AUTHORIZATION) String authorization, @QueryMap RequestParams gpaParams, Callback<JsonElement> response);

    @FormUrlEncoded
    @POST("/gpa/evaluate")
    void postGPAEvaluate(@Header(AUTHORIZATION) String authorization, @Query("token") String token, @FieldMap RequestParams params, Callback<JsonElement> response);

}
