package com.twt.wepeiyang.commons.auth.login;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.auth.AuthApi;
import com.twt.wepeiyang.commons.network.ApiErrorHandler;
import com.twt.wepeiyang.commons.network.ApiResponse;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import rx.Notification;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2016/11/27.
 */

public class LoginViewModel implements ViewModel {

    //context
    private RxAppCompatActivity mActivity;

    //model
    public final ObservableField<String> twtuName = new ObservableField<>();
    public final ObservableField<String> twtpasswd = new ObservableField<>();
    public Token mToken;

    public LoginViewModel(RxAppCompatActivity activity) {
        mActivity = activity;
    }

    //viewStyle
    public final ViewStyle mViewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isProgressRefreshing = new ObservableBoolean(false);
    }

    //command
    public final ReplyCommand onLoginClickCommand = new ReplyCommand(this::login);

    public final ReplyCommand onLaterLoginCommand = new ReplyCommand(this::laterLogin);

    public final ReplyCommand onForgetPasswordCommand = new ReplyCommand(() -> {
        // TODO: 2017/1/19 fogetpassword jump to act
        Toast.makeText(mActivity, "fogetpassword", Toast.LENGTH_SHORT).show();
    });

    public final ReplyCommand onRegisterClickCommand = new ReplyCommand(() -> {
        // TODO: 2016/11/27 跳转到注册页面
        Toast.makeText(mActivity, "jump to register page", Toast.LENGTH_SHORT).show();
    });

    private void login() {
        mViewStyle.isProgressRefreshing.set(true);

        Observable<Notification<Token>> wpyToken = RetrofitProvider.getRetrofit()
                .create(AuthApi.class)
                .login(twtuName.get(), twtpasswd.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mActivity.bindToLifecycle())
                .map(ApiResponse::getData)
                .materialize()
                .share();

        wpyToken.filter(Notification::isOnNext)
                .map(Notification::getValue)
                .doAfterTerminate(() -> mViewStyle.isProgressRefreshing.set(false))
                .subscribe(token -> {
                    CommonPrefUtil.setToken(token.token);
                    CommonPrefUtil.setIsLogin(true);
                    Toast.makeText(mActivity, "登陆成功", Toast.LENGTH_SHORT).show();
                    // TODO: 2016/11/27 jump to home page
                    Class clazz = null;
                    try {
                        clazz = Class.forName("com.twtstudio.retrox.wepeiyangrd.home.HomeActivity");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(mActivity, clazz);
                    mActivity.startActivity(intent);
                });

        Observable<Throwable> throwableObservable =
                wpyToken.filter(Notification::isOnError)
                        .map(Notification::getThrowable);

        new ApiErrorHandler(mActivity).handleError(throwableObservable);

    }

    private void laterLogin() {
        Toast.makeText(mActivity, "laterloginClicked", Toast.LENGTH_SHORT).show();
    }
}
