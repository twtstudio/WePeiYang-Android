package com.twt.wepeiyang.commons.user;

import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.crashreport.CrashReport;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 14/10/2017.
 */

public class UserManager {
    private static AuthApi authApi = RetrofitProvider.getRetrofit().create(AuthApi.class);

    /**
     * 登录逻辑
     * 并且存储用户信息
     * @param username      用户账号
     * @param password      用户密码
     * @param detailObserver 回调接口
     */
    public static void login(String username, String password, Observer<AuthSelfBean> detailObserver) {
        authApi.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(tokenApiResponse -> detailObserver.onNext(tokenApiResponse.getData()))
                .doOnNext(tokenApiResponse -> {
                    CommonPrefUtil.setToken(tokenApiResponse.getData().token);

                })
                .observeOn(Schedulers.io())
                .flatMap(tokenApiResponse -> authApi.getAuthSelf())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authSelfBean -> {

                    /*
                      进行一些数据持久化处理
                     */
                    CommonPrefUtil.setStudentNumber(authSelfBean.studentid);
                    CommonPrefUtil.setIsBindLibrary(authSelfBean.accounts.lib);
                    CommonPrefUtil.setIsBindTju(authSelfBean.accounts.tju);
                    //user id
                    CrashReport.setUserId(authSelfBean.twtuname);

                    //放置缓存
                    Hawk.put("authSelfBean", authSelfBean);

                    int dropOutMode = authSelfBean.dropout;

                    CommonPrefUtil.setDropOut(dropOutMode); //0=未操作，1=已退学，2=已复学
                    CommonPrefUtil.setIsLogin(true);
                    CommonPrefUtil.setIsFirstLogin(false);
                    detailObserver.onNext(authSelfBean);

                }, throwable -> {
                    new RxErrorHandler().call(throwable);
                    detailObserver.onError(throwable);
                });
    }

    /**
     * 获取用户的详细详细
     * @return
     */
    public static AuthSelfBean getUserDetail(){
        AuthSelfBean bean =  Hawk.get("authSelfBean");
        if (bean == null){
            throw new NullPointerException("You Must Login !!!");
        }
        return bean;
    }

    public static String getToken(){
        return CommonPrefUtil.getToken();
    }

    public static Boolean getIsLogin(){
        return CommonPrefUtil.getIsLogin();
    }

    public static Boolean getIsFirstLogin(){
        return CommonPrefUtil.getIsFirstLogin();
    }
}
