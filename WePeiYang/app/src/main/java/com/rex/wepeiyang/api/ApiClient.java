package com.rex.wepeiyang.api;

import com.rex.wepeiyang.bean.Gpa;
import com.rex.wepeiyang.bean.News;
import com.rex.wepeiyang.bean.NewsList;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by Rex on 2015/8/1.
 */
public class ApiClient {

    public static final String API = "http://open.twtstudio.com/api/v1";
    private static RestAdapter adapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(API).build();
    private static Api mApi = adapter.create(Api.class);

    public static void getGpaWithoutToken(String tjuuname, String tjupasswd, Callback<Object> callback) {
        RequestParams params = new RequestParams();
        params.put("tjuuname", tjuuname);
        params.put("tjupasswd", tjupasswd);
        HashMap<String, String> temp = new HashMap<>();
        temp.put("t", params.get("t"));
        temp.put("tjuuname", tjuuname);
        temp.put("tjupasswd", tjupasswd);
        String sign = Sign.generate(temp);
        params.put("sign", sign);
        mApi.getGPA(params, callback);
    }

    public static void getGpaWithToken(String tjuuname, String tjupasswd, String token, String captcha, Callback<Object> callback){
        RequestParams params = new RequestParams();
        params.put("tjuuname", tjuuname);
        params.put("tjupasswd", tjupasswd);
        params.put("token", token);
        params.put("captcha", captcha);
        HashMap<String, String> temp = new HashMap<>();
        temp.put("t", params.get("t"));
        temp.put("tjuuname", tjuuname);
        temp.put("tjupasswd", tjupasswd);
        params.put("token", token);
        params.put("captcha", captcha);
        String sign = Sign.generate(temp);
        params.put("sign", sign);
        mApi.getGPA(params, callback);
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


    public static void getLost() {

    }

    public static void getFound() {

    }
}
