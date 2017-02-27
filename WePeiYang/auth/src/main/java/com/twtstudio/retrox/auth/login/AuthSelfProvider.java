package com.twtstudio.retrox.auth.login;

import com.twt.wepeiyang.commons.network.RetrofitProvider;

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
                .subscribe(action1,Throwable::printStackTrace);

    }
}
