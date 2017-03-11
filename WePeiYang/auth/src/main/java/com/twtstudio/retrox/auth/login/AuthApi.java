package com.twtstudio.retrox.auth.login;



import com.twt.wepeiyang.commons.network.ApiResponse;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by retrox on 2017/1/31.
 */

public interface AuthApi {
    /**
     * 获取微北洋token
     * @param twtuname 用户名
     * @param twtpasswd 密码
     * @return
     */
    @GET("auth/token/get")
    Observable<ApiResponse<Token>> login(@Query("twtuname") String twtuname, @Query("twtpasswd") String twtpasswd);

    @GET("http://open.twtstudio.com/api/v2/auth/self")
    Observable<AuthSelfBean> getAuthSelf();

    @GET("auth/token/refresh")
    Observable<ApiResponse<Token>> refreshToken();

}
