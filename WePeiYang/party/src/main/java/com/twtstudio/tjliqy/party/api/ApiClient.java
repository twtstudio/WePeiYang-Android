package com.twtstudio.tjliqy.party.api;

import android.util.Log;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.tjliqy.party.bean.CourseDetailInfo;
import com.twtstudio.tjliqy.party.bean.QuizInfo;
import com.twtstudio.tjliqy.party.bean.Status;
import com.twtstudio.tjliqy.party.bean.TextDetailInfo;
import com.twtstudio.tjliqy.party.bean.UserInfomation;

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
        Log.d("2333", "loadUserInfomation: "+CommonPrefUtil.getToken());
        String token = CommonPrefUtil.getToken();
//        token = token.split("\\}")[0];
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

    public static void getQuiz(String sno, int courseId, Callback<QuizInfo> callback){
        Call<QuizInfo> call = api.getQuestion("api","20course_test",sno,courseId);
        call.enqueue(callback);
    }

    public static void submitAnswer(String sno, int courseId, String rightAnswer, String exerciseAnswer, Callback<Status> callback){
        Call<Status> call = api.submitAnswer("api","20course_test",sno,courseId,rightAnswer,exerciseAnswer,"success");
        call.enqueue(callback);
    }
}
