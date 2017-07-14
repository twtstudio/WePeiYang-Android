package com.twtstudio.retrox.gpa.client;

import okhttp3.ResponseBody;
import retrofit2.Call;
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


    @GET("gpa")
    Call<ResponseBody> getGPA(@Header(AUTHORIZATION) String authorization, @QueryMap RequestParams gpaParams);

    @FormUrlEncoded
    @POST("gpa/evaluate")
    Call<ResponseBody> postGPAEvaluate(@Header(AUTHORIZATION) String authorization, @Query("token") String token, @FieldMap RequestParams params);

}
