package com.twt.service.api;

import com.google.gson.JsonElement;
import com.twt.service.bean.ClassTable;
import com.twt.service.bean.Login;
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

;

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

    @GET("/news/{type}/page/{page}")
    void getNewsList(@Path("type") int type, @Path("page") int page, Callback<NewsList> response);

    @GET("/news/{index}")
    void getNews(@Path("index") int index, Callback<News> response);

    @GET("/app/index")
    void getMain(Callback<JsonElement> response);

    @GET("/auth/token/get")
    void login(@QueryMap HashMap<String, String> loginParams, Callback<JsonElement> response);

    @GET("/auth/token/refresh")
    void refreshToken(@Header("Authorization") String authorization, @QueryMap HashMap<String, String> refreshParams, Callback<RefreshedToken> response);

    @GET("/app/feedback")
    void feedback(@Header("User-Agent") String ua, @QueryMap HashMap<String, String> feedbackParams, Callback<JsonElement> response);

    @GET("/auth/unbind/tju")
    void unbindTju(@Header("Authorization") String authorization, @QueryMap HashMap<String, String> unbindParams, Callback<JsonElement> response);


}
