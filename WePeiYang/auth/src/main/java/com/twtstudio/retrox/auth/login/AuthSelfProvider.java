package com.twtstudio.retrox.auth.login;

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

    public void getUserData(Action1<AuthSelfBean> action1){

        AuthApi authApi = RetrofitProvider.getRetrofit().create(AuthApi.class);
        authApi.getAuthSelf().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authSelfBean -> {
                    /**
                     * 进行一些数据持久化处理
                     */
                    CommonPrefUtil.setStudentNumber(authSelfBean.studentid);
                    CommonPrefUtil.setIsBindLibrary(authSelfBean.accounts.lib);
                    CommonPrefUtil.setIsBindTju(authSelfBean.accounts.tju);
                    //user id
                    CrashReport.setUserId(authSelfBean.twtuname);

                    if (action1 != null) {
                        action1.call(authSelfBean);
                    }
                },Throwable::printStackTrace);

    }


}
