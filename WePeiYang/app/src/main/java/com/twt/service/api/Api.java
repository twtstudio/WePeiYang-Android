package com.twt.service.api;

import com.google.gson.JsonElement;
import com.twt.service.bean.ClassTable;
import com.twt.service.bean.Found;
import com.twt.service.bean.Jobs;
import com.twt.service.bean.JobsList;
import com.twt.service.bean.Login;
import com.twt.service.bean.Lost;
import com.twt.service.bean.Main;
import com.twt.service.bean.News;
import com.twt.service.bean.NewsList;
import com.twt.service.bean.RefreshedToken;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by sunjuntao on 15/11/5.
 */
public interface Api {

    @GET("/auth/bind/tju")
    void bind(@Header("Authorization") String authorization, @QueryMap HashMap<String, String> bindParams, Callback<JsonElement> response);

    @GET("/gpa")
    void getGPA(@Header("Authorization") String authorization, @QueryMap HashMap<String, String> gpaParams, Callback<JsonElement> response);

    @GET("/classtable")
    void getCourse(@Header("Authorization") String authorization, @QueryMap HashMap<String, String> classtableParams, Callback<JsonElement> response);

    @GET("/news/type/{type}")
    void getNewsList(@Path("type") int type, @QueryMap HashMap<String, String> newsParams, Callback<NewsList> response);

    @GET("/news/{index}")
    void getNews(@Path("index") int index, @QueryMap HashMap<String, String> newsParams, Callback<News> response);

    @GET("/app/index")
    void getMain(@QueryMap HashMap<String, String> mainParams, Callback<JsonElement> response);

    @GET("/auth/token/get")
    void login(@QueryMap HashMap<String, String> loginParams, Callback<JsonElement> response);

    @GET("/auth/token/refresh")
    void refreshToken(@Header("Authorization") String authorization, @QueryMap HashMap<String, String> refreshParams, Callback<RefreshedToken> response);

    @GET("/app/feedback")
    void feedback(@Header("User-Agent") String ua, @QueryMap HashMap<String, String> feedbackParams, Callback<JsonElement> response);

    @GET("/auth/unbind/tju")
    void unbindTju(@Header("Authorization") String authorization, @QueryMap HashMap<String, String> unbindParams, Callback<JsonElement> response);

    @GET("/lostfound/lost")
    void getLostList(@QueryMap HashMap<String, String> lostParams, Callback<Lost> response);

    @GET("/lostfound/found")
    void getFoundList(@QueryMap HashMap<String, String> foundParams, Callback<Found> response);

    @GET("/jobs")
    void getJobsList(@QueryMap HashMap<String, String> jobsParams, Callback<JobsList> response);

    @GET("/jobs/{id}")
    void getJobsDetails(@QueryMap HashMap<String, String> jobsDetailsParams, @Path("id") int id, Callback<Jobs> response);


}
