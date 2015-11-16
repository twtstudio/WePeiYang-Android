package com.rex.wepeiyang.api;

import com.rex.wepeiyang.bean.Gpa;
import com.rex.wepeiyang.bean.News;
import com.rex.wepeiyang.bean.NewsList;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by Rex on 2015/8/1.
 */
public class ApiClient {

    public static final String API = "http://open.twtstudio.com/api/v1";
    private static RestAdapter adapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(API).build();
    private static Api mApi = adapter.create(Api.class);

    public static void getGpa(String tjuuname, String tjupasswd, Callback<Gpa> callback) {
        RequestParams params = new RequestParams();
        params.put("tjuuname", tjuuname);
        params.put("tjupasswd", tjupasswd);
        String sign = Sign.generate(params);
        params.put("sign", sign);
        mApi.getGPA(params, callback);
    }

    public static void getImportantNewsList(int page, Callback<NewsList> callback) {
        mApi.getNewsList(1, page, callback);
    }

    public static void getNoticeList(int page, Callback<NewsList> callback) {
        mApi.getNewsList(2, page, callback);
    }

    public static void getNewsDetails(int index, Callback<News> callback) {
        mApi.getNews(index, callback);
    }


    public static void getLost() {

    }

    public static void getFound() {

    }
}
