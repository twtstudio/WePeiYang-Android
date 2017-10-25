package com.twtstudio.retrox.tjulibrary.provider;

import com.twt.wepeiyang.commons.network.ApiResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by retrox on 2017/2/21.
 */

public interface LibApi {

    @GET("library/user/info")
    Observable<ApiResponse<Info>> getLibUserInfo();

    @GET("library/user/history")
    Observable<ApiResponse<List<Histroy>>> getLibUserHistroy();

    @GET("library/renew/{barcode}")
    Observable<ApiResponse<List<RenewResult>>> renewBooks(@Path("barcode") String barcode);

    @GET("auth/bind/lib")
    Observable<ApiResponse<String>> bindLib(@Query("libpasswd") String libpasswd);

    @GET("auth/unbind/lib")
    Observable<ApiResponse<String>> unbindLib();

    @GET("library/book")
    Observable<ResponseBody> searchLibBook(@Query("title") String name, @Query("page") Integer page);

    @GET("library/book/{id}")
    Observable<ResponseBody> getBookDetail(@Path("id") String id);

}
