package com.twt.service.party.api;

import com.twt.service.party.bean.CourseDetailInfo;
import com.twt.service.party.bean.Status;
import com.twt.service.party.bean.TextDetailInfo;
import com.twt.service.party.bean.UserInfomation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tjliqy on 2016/7/19.
 */
public interface Api {


    @GET("auth/self/")
    Call<UserInfomation> getInfomation(@Query("token") String token);

    @GET("party/")
    Call<Status> getStatus(@Query("page") String page, @Query("do") String doWhat, @Query("sno") String sno);

    @GET("party/")
    Call<Status> signTest(@Query("page") String page, @Query("do") String doWhat, @Query("sno") String sno, @Query("test_id") int testId);

    @GET("party/")
    Call<CourseDetailInfo> getCourseDetail(@Query("page") String page, @Query("do") String doWhat, @Query("sno") String sno, @Query("course_id") int courseId);

    @GET("party/")
    Call<TextDetailInfo> getTextDetail(@Query("page") String page, @Query("do") String doWhat, @Query("sno") String sno, @Query("text_id") int textId);

    @GET("party/")
    Call<Status> appeal(@Query("page") String page, @Query("do") String doWhat, @Query("sno") String sno, @Query("test_id") int testId, @Query("title") String title, @Query("content") String content);

}
