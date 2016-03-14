package com.twt.service.api;

import com.google.gson.JsonElement;
import com.twt.service.bean.CommentCallback;
import com.twt.service.bean.Found;
import com.twt.service.bean.FoundDetails;
import com.twt.service.bean.Jobs;
import com.twt.service.bean.JobsList;
import com.twt.service.bean.Lost;
import com.twt.service.bean.LostDetails;
import com.twt.service.bean.News;
import com.twt.service.bean.NewsList;
import com.twt.service.bean.RefreshedToken;
import com.twt.service.bean.Upload;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;

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

    @POST("/news/comment/{id}")
    void postNewsComment(@Header("Authorization") String authorization, @Path("id") int id, @QueryMap HashMap<String, String> commentParams, Callback<CommentCallback> response);

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

    @GET("/lostfound/{id}")
    void getLostDetails(@Path("id") int id, @QueryMap HashMap<String, String> params, Callback<LostDetails> response);

    @GET("/lostfound/{id}")
    void getFoundDetails(@Path("id") int id, @QueryMap HashMap<String, String> params, Callback<FoundDetails> response);

    @GET("/jobs")
    void getJobsList(@QueryMap HashMap<String, String> jobsParams, Callback<JobsList> response);

    @GET("/jobs/{id}")
    void getJobsDetails(@QueryMap HashMap<String, String> jobsDetailsParams, @Path("id") int id, Callback<Jobs> response);

    @POST("/lostfound/lost")
    void postLost(@Header("Authorization") String authorization, @QueryMap HashMap<String, String> params, Callback<JsonElement> response);

    @POST("/lostfound/found")
    void postFound(@Header("Authorization") String authorization, @QueryMap HashMap<String, String> params, Callback<JsonElement> response);

    @Multipart
    @GET("/app/upload")
    void uploadImage(@Part("to") String to, @Part("file") TypedFile file, Callback<Upload> response);
}
