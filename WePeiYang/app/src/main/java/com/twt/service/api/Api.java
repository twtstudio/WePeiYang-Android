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

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by sunjuntao on 15/11/5.
 */
public interface Api {

    static final String AUTHORIZATION = "Authorization";

    @GET("/auth/bind/tju")
    void bind(@Header(AUTHORIZATION) String authorization, @QueryMap RequestParams bindParams, Callback<JsonElement> response);

    @GET("/gpa")
    void getGPA(@Header(AUTHORIZATION) String authorization, @QueryMap RequestParams gpaParams, Callback<JsonElement> response);

    @GET("/classtable")
    void getCourse(@Header(AUTHORIZATION) String authorization, @QueryMap RequestParams classtableParams, Callback<JsonElement> response);

    @GET("/news/type/{type}")
    void getNewsList(@Path("type") int type, @QueryMap RequestParams newsParams, Callback<NewsList> response);

    @GET("/news/{index}")
    void getNews(@Path("index") int index, @QueryMap RequestParams newsParams, Callback<News> response);

    @FormUrlEncoded
    @POST("/news/comment/{id}")
    void postNewsComment(@Header(AUTHORIZATION) String authorization, @Path("id") int id, @FieldMap RequestParams commentParams, Callback<CommentCallback> response);

    @GET("/app/index")
    void getMain(@QueryMap RequestParams mainParams, Callback<JsonElement> response);

    @GET("/auth/token/get")
    void login(@QueryMap RequestParams loginParams, Callback<JsonElement> response);

    @GET("/auth/token/refresh")
    void refreshToken(@Header(AUTHORIZATION) String authorization, @QueryMap RequestParams refreshParams, Callback<RefreshedToken> response);

    @GET("/app/feedback")
    void feedback(@Header("User-Agent") String ua, @QueryMap RequestParams feedbackParams, Callback<JsonElement> response);

    @GET("/auth/unbind/tju")
    void unbindTju(@Header(AUTHORIZATION) String authorization, @QueryMap RequestParams unbindParams, Callback<JsonElement> response);

    @GET("/lostfound/lost")
    void getLostList(@QueryMap RequestParams lostParams, Callback<Lost> response);

    @GET("/lostfound/found")
    void getFoundList(@QueryMap RequestParams foundParams, Callback<Found> response);

    @GET("/lostfound/{id}")
    void getLostDetails(@Path("id") int id, @QueryMap RequestParams params, Callback<LostDetails> response);

    @GET("/lostfound/{id}")
    void getFoundDetails(@Path("id") int id, @QueryMap RequestParams params, Callback<FoundDetails> response);

    @GET("/jobs")
    void getJobsList(@QueryMap RequestParams jobsParams, Callback<JobsList> response);

    @GET("/jobs/{id}")
    void getJobsDetails(@QueryMap RequestParams jobsDetailsParams, @Path("id") int id, Callback<Jobs> response);

    @FormUrlEncoded
    @POST("/lostfound/lost")
    void postLost(@Header(AUTHORIZATION) String authorization, @FieldMap RequestParams params, Callback<JsonElement> response);

    @FormUrlEncoded
    @POST("/lostfound/found")
    void postFound(@Header(AUTHORIZATION) String authorization, @FieldMap RequestParams params, Callback<JsonElement> response);

    @Multipart
    @POST("/app/upload")
    void uploadImage(@PartMap Map<String, RequestBody> requestBodyMap, Callback<List<Upload>> response);

    @GET("/lostfound/user/lost")
    void getMyLostList(@Header(AUTHORIZATION) String authorization, @QueryMap RequestParams params, Callback<Lost> response);

    @GET("/lostfound/user/found")
    void getMyFoundList(@Header(AUTHORIZATION) String authorization, @QueryMap RequestParams params, Callback<Found> response);

    @FormUrlEncoded
    @PUT("/lostfound/lost/{id}")
    void editLost(@Header(AUTHORIZATION) String authorization, @Path("id") int id, @FieldMap RequestParams params, Callback<JsonElement> response);

    @FormUrlEncoded
    @PUT("/lostfound/found/{id}")
    void editFound(@Header(AUTHORIZATION) String authorization, @Path("id") int id, @FieldMap RequestParams params, Callback<JsonElement> response);
}
