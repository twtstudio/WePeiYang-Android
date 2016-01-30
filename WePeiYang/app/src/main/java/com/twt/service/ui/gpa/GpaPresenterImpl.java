package com.twt.service.ui.gpa;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.twt.service.bean.*;
import com.twt.service.bean.RestError;
import com.twt.service.db.JsonDataSupport;
import com.twt.service.interactor.GpaInteractor;
import com.twt.service.support.ACache;
import com.twt.service.support.PrefUtils;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/11/22.
 */
public class GpaPresenterImpl implements GpaPresenter, OnGetGpaCallback, OnRefreshTokenCallback {

    private GpaView view;
    private GpaInteractor interactor;
    private Context context;

    public GpaPresenterImpl(GpaView view, GpaInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void getGpaWithoutToken() {
        view.showProgress();
        Log.e("Token", PrefUtils.getToken());
        interactor.getGpaWithoutToken(PrefUtils.getToken(), this);
    }

    @Override
    public void getGpaWithToken(String token, String captcha) {
        view.showProgress();
        interactor.getGpaWithToken(PrefUtils.getToken(), token, captcha, this);
    }

    @Override
    public void getGpaFromNet() {
        getGpaWithoutToken();
    }

    @Override
    public void getGpaFromCache() {
        final ACache cache = ACache.get(context);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String gpaString = cache.getAsString("gpa");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (gpaString == null) {
                            getGpaFromNet();
                        } else {
                            Gpa gpa = new Gson().fromJson(gpaString, Gpa.class);
                            view.bindData(gpa);
                            view.setClickable(true);
                        }
                    }
                });
            }
        }).start();
    }


    @Override
    public void onSuccess(String gpaString) {
        view.hideProgress();
        final String json = gpaString;
        if (json.contains("token")) {
            GpaCaptcha gpaCaptcha = new Gson().fromJson(json, GpaCaptcha.class);
            view.showCaptchaDialog(gpaCaptcha);
        } else {
            view.setClickable(true);
            final ACache cache = ACache.get(context);
            Gpa gpa = new Gson().fromJson(json, Gpa.class);
            view.bindData(gpa);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    cache.put("gpa", json, 2 * ACache.TIME_DAY);
                }
            }).start();
        }
    }

    @Override
    public void onFailure(RetrofitError retrofitError) {
        view.hideProgress();
        switch (retrofitError.getKind()) {
            case HTTP:
                RestError error = (RestError) retrofitError.getBodyAs(RestError.class);
                if (error != null) {
                    switch (error.error_code) {
                        case 10000:
                            view.toastMessage("请重新登录");
                            PrefUtils.setLogin(false);
                            PrefUtils.setToken(null);
                            view.startLoginActivity();
                            break;
                        case 10001:
                            view.toastMessage("请重新登录");
                            PrefUtils.setLogin(false);
                            PrefUtils.setToken(null);
                            view.startLoginActivity();
                            break;
                        case 10002:
                            view.toastMessage("请重新登录");
                            PrefUtils.setLogin(false);
                            PrefUtils.setToken(null);
                            view.startLoginActivity();
                            break;
                        case 10003:
                            Log.e("refresh", "refresh");
                            interactor.refreshToken(PrefUtils.getToken(), this);
                            break;
                        case 20001:
                            view.toastMessage("请绑定办公网帐号");
                            view.startBindActivity();
                            break;
                        default:
                            view.toastMessage(error.message);
                            break;
                    }
                }
                break;
            case NETWORK:
                view.toastMessage("无法连接到网络");
                break;
            case CONVERSION:
            case UNEXPECTED:
                throw retrofitError;
            default:
                throw new AssertionError("未知的错误类型：" + retrofitError.getKind());
        }
    }

    @Override
    public void onSuccess(RefreshedToken refreshedToken) {
        PrefUtils.setLogin(true);
        PrefUtils.setToken("Bearer {" + refreshedToken.data + "}");
        interactor.getGpaWithoutToken("Bearer {" + refreshedToken.data + "}", this);
    }

    @Override
    public void onFailure() {
        view.toastMessage("请重新登录");
        PrefUtils.setLogin(false);
        PrefUtils.setToken(null);
        view.startLoginActivity();
    }
}
