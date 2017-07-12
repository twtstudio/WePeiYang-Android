package com.twtstudio.retrox.gpa.client;

import com.twt.wepeiyang.commons.network.UserAgent;

import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by Rex on 2015/8/1.
 */
public class ApiClient {

    private static Interceptor requestInterceptor = chain -> {
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder();
        builder.addHeader("User-Agent", UserAgent.generate());
        Request request = builder.build();
        return chain.proceed(request);
    };
    static OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .build();
    private static final String API = "http://open.twtstudio.com/api/v1/";

    static Retrofit mRetrofit = new Retrofit.Builder()
            .client(mOkHttpClient)
            .baseUrl(API)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    private static Api mApi = mRetrofit.create(Api.class);

    public void postGpaEvaluate(String authorization, String token, String lessonId, String unionId, String courseId, String term, int[] fiveQ, String note, Callback<ResponseBody> callback) {
        RequestParams params = new RequestParams();
        params.put("lesson_id", lessonId);
        params.put("union_id", unionId);
        params.put("course_id", courseId);
        params.put("term", term);
        params.put("q1", String.valueOf(fiveQ[0]));
        params.put("q2", String.valueOf(fiveQ[1]));
        params.put("q3", String.valueOf(fiveQ[2]));
        params.put("q4", String.valueOf(fiveQ[3]));
        params.put("q5", String.valueOf(fiveQ[4]));
        params.put("note", note);
        HashMap<String, String> temp = new HashMap<>();
        temp.put("t", params.get("t"));
        temp.put("token", token);
        temp.put("lesson_id", lessonId);
        temp.put("union_id", unionId);
        temp.put("course_id", courseId);
        temp.put("term", term);
        temp.put("q1", String.valueOf(fiveQ[0]));
        temp.put("q2", String.valueOf(fiveQ[1]));
        temp.put("q3", String.valueOf(fiveQ[2]));
        temp.put("q4", String.valueOf(fiveQ[3]));
        temp.put("q5", String.valueOf(fiveQ[4]));
        temp.put("note", note);
        String sign = new Sign().generate(temp);
        params.put("sign", sign);
        authorization = "Bearer{" + authorization + "}";
        mApi.postGPAEvaluate(authorization, token, params).enqueue(callback);
    }

}
