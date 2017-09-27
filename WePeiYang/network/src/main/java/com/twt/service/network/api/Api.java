package com.twt.service.network.api;

import com.twt.service.network.modle.RequestParam;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chen on 2017/7/9.
 */

public interface Api {
    //前面这些都是鹏鹏爬的api
//    @GET("network/login")
//    Observable<ApiResponse<LoginBean>> login(@Query("username") String username, @Query("password") String password);
//
//    @GET("network/logout")
//    Observable<LoginBean> logoutTry(@Query("username") String username, @Query("password") String password);
//
//    @GET("network/status")
//    Observable<List<StatusBean.data>> getStatus(@Path("data") String data);

//    @GET("network/ip")
//    Observable<ApiResponse<List<IPBean.data>>> getIp(@Path("data") String data);

//    @GET("network/logout")
//    Observable<ApiResponse<LoginBean>> logout(@Query("username") String username, @Query("password") String password);

    //下面是直接从登录界面获取的
    @FormUrlEncoded
    @POST("include/auth_action.php")
    Observable<ResponseBody> logoutPost(@Field("username") String username, @Field("password") String password, @Field("action") String action, @Field("ajax") int ajax);

    @FormUrlEncoded
    @POST("include/auth_action.php")
    Observable<ResponseBody> loginPost(@Field("username") String username, @Field("password") String password, @Field("action") String action, @Field("ac_id") String acId, @FieldMap RequestParam requestParam, @Query("ajax") String ajax);

    @GET("http://119.75.213.61/")
    Observable<ResponseBody> baidu();

}
