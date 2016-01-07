package com.rex.wepeiyang.api;

import android.util.Log;

import com.google.gson.JsonElement;
import com.rex.wepeiyang.bean.ClassTable;
import com.rex.wepeiyang.bean.Login;
import com.rex.wepeiyang.bean.Main;
import com.rex.wepeiyang.bean.News;
import com.rex.wepeiyang.bean.NewsList;
import com.rex.wepeiyang.support.PrefUtils;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by Rex on 2015/8/1.
 */
public class ApiClient {

    public static final String API = "http://open.twtstudio.com/api/v1";
    private static RestAdapter adapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(API).build();
    private static Api mApi = adapter.create(Api.class);


    public static void bind(String authorization, String tjuname, String tjupwd, Callback<JsonElement> callback) {
        RequestParams params = new RequestParams();
        params.put("tjuuname", tjuname);
        params.put("tjupasswd", tjupwd);
        HashMap<String, String> temp = new HashMap<>();
        temp.put("t", params.get("t"));
        temp.put("tjuuname", tjuname);
        temp.put("tjupasswd", tjupwd);
        String sign = new Sign().generate(temp);
        params.put("sign", sign);
        mApi.bind(authorization, params, callback);
    }

    public static void refreshToken(String authorization, Callback<JsonElement> callback) {
        RequestParams params = new RequestParams();
        HashMap<String, String> temp = new HashMap<>();
        temp.put("t", params.get("t"));
        String sign = new Sign().generate(temp);
        params.put("sign", sign);
        mApi.refreshToken(authorization, params, callback);
    }

    public static void login(String twtuname, String twtpasswd, Callback<Login> callback) {
        RequestParams params = new RequestParams();
        params.put("twtuname", twtuname);
        params.put("twtpasswd", twtpasswd);
        HashMap<String, String> temp = new HashMap<>();
        temp.put("t", params.get("t"));
        temp.put("twtuname", twtuname);
        temp.put("twtpasswd", twtpasswd);
        String sign = new Sign().generate(temp);
        params.put("sign", sign);
        mApi.login(params, callback);
    }

    public static void getGpaWithoutToken(String authorization, Callback<JsonElement> callback) {
        RequestParams params = new RequestParams();
        HashMap<String, String> temp = new HashMap<>();
        temp.put("t", params.get("t"));
        String sign = new Sign().generate(temp);
        params.put("sign", sign);
        mApi.getGPA(authorization, params, callback);
    }

    public static void getGpaWithToken(String authorization, String token, String captcha, Callback<JsonElement> callback) {
        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("captcha", captcha);
        HashMap<String, String> temp = new HashMap<>();
        temp.put("t", params.get("t"));
        temp.put("token", token);
        temp.put("captcha", captcha);
        String sign = new Sign().generate(temp);
        params.put("sign", sign);
        mApi.getGPA(authorization, params, callback);
    }

    public static void getClassTable(String authorization, Callback<ClassTable> callback) {
        RequestParams params = new RequestParams();
        HashMap<String, String> temp = new HashMap<>();
        temp.put("t", params.get("t"));
        String sign = new Sign().generate(temp);
        params.put("sign", sign);
        mApi.getCourse(authorization, params, callback);
    }

    public static void getImportantNewsList(int page, Callback<NewsList> callback) {
        mApi.getNewsList(1, page, callback);
    }

    public static void getNoticeList(int page, Callback<NewsList> callback) {
        mApi.getNewsList(2, page, callback);
    }

    public static void getViewPointList(int page, Callback<NewsList> callback) {
        mApi.getNewsList(5, page, callback);
    }

    public static void getAssociationList(int page, Callback<NewsList> callback) {
        mApi.getNewsList(3, page, callback);
    }

    public static void getCollegeNewslist(int page, Callback<NewsList> callback) {
        mApi.getNewsList(4, page, callback);
    }

    public static void getNewsDetails(int index, Callback<News> callback) {
        mApi.getNews(index, callback);
    }

    public static void getMain(Callback<Main> callback) {
        mApi.getMain(callback);
    }

    public static void getLost() {

    }

    public static void getFound() {

    }
}
