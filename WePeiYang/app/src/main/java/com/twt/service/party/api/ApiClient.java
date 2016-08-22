package com.twt.service.party.api;

import android.util.Log;

import com.twt.service.party.bean.CourseDetailInfo;
import com.twt.service.party.bean.Status;
import com.twt.service.party.bean.TextDetailInfo;
import com.twt.service.party.bean.UserInfomation;
import com.twt.service.support.PrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tjliqy on 2016/7/19.
 */
public class ApiClient {

    private static Api api;
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://www.twt.edu.cn/")
                .build();
        api = retrofit.create(Api.class);
    }
    public static void loadUserInfomation(Callback<UserInfomation> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://open.twtstudio.com/api/v2/")
                .build();
        Api api = retrofit.create(Api.class);
        String token = PrefUtils.getToken().split("\\{")[1];
        token = token.split("\\}")[0];
        Log.d("lqy",token);
        Call<UserInfomation> call = api.getInfomation(token);
        call.enqueue(callback);
    }

    public static void loadStatus(String doWhat, String sno, Callback<Status> callback){
        Call<Status> call= api.getStatus("api",doWhat,sno);
        call.enqueue(callback);
    }

    public static void signTest(String sno, String doWhat, int testId,  Callback<Status> callback){
        Call<Status> call = api.signTest("api",doWhat, sno, testId);
        call.enqueue(callback);
    }

    public static void loadCourseDetail(String sno, int courseId, Callback<CourseDetailInfo> callback){
        Call<CourseDetailInfo> call = api.getCourseDetail("api","applicant_coursestudy_detail", sno, courseId);
        call.enqueue(callback);
    }

    public static void loadTextDetail(String sno, int textId, Callback<TextDetailInfo> callback){
        Call<TextDetailInfo> call = api.getTextDetail("api", "study_textArticle", sno, textId);
        call.enqueue(callback);
    }
    public static void appeal(String sno,String doWhat, int test_id, String title, String content, Callback<Status> callback){
        Call<Status> call = api.appeal("api",doWhat,sno,test_id,title,content);
        call.enqueue(callback);
    }

    public static void submit(String sno, String title, String content, String type, Callback<Status> callback){
        Call<Status> call = api.submit("api", "fileupload", sno, title, content,type,"submit");
        call.enqueue(callback);
    }
}
