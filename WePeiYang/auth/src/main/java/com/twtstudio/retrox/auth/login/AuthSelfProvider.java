package com.twtstudio.retrox.auth.login;

import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.crashreport.CrashReport;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/2/20.
 */

public class AuthSelfProvider {

    public void getUserData(Action1<AuthSelfBean> action1) {

        //贼简单的一个缓存逻辑
        AuthSelfBean cacheAuthSelfBean = Hawk.get("authSelfBean");
        if (cacheAuthSelfBean != null && action1 != null) {
            action1.call(cacheAuthSelfBean);
        }

        AuthApi authApi = RetrofitProvider.getRetrofit().create(AuthApi.class);
        authApi.getAuthSelf().subscribeOn(Schedulers.io())
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

                    if (action1 != null) {
                        action1.call(authSelfBean);
                    }
                }, Throwable::printStackTrace);

    }

    public void getUserData() {
        this.getUserData(null);
    }


}
