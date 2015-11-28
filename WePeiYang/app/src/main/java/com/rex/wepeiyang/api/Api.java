package com.rex.wepeiyang.api;

import com.google.gson.JsonElement;
import com.rex.wepeiyang.bean.ClassTable;
import com.rex.wepeiyang.bean.Feedback;
import com.rex.wepeiyang.bean.Gpa;
import com.rex.wepeiyang.bean.News;
import com.rex.wepeiyang.bean.NewsList;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;

;

/**
 * Created by sunjuntao on 15/11/5.
 */
public interface Api {

    @GET("/tmp/gpa/")
    void getGPA(@QueryMap HashMap<String, String> gpaParams, Callback<JsonElement> response);

    @GET("/tmp/classtable")
    void getCourse(@QueryMap HashMap<String, String> classtableParams, Callback<ClassTable> response);

    @GET("/app/feedback/")
    void feedback(@QueryMap HashMap<String, String> feedbackParams, Callback<Feedback> response);

    @GET("/news/{type}/page/{page}")
    void getNewsList(@Path("type") int type, @Path("page") int page, Callback<NewsList> response);

    @GET("/news/{index}")
    void getNews(@Path("index") int index, Callback<News> response);

}
