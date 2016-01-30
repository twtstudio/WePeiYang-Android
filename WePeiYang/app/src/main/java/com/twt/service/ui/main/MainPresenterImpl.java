package com.twt.service.ui.main;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.twt.service.bean.Main;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.MainInteractor;
import com.twt.service.support.ACache;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 15/12/3.
 */
public class MainPresenterImpl implements MainPresenter, OnGetMainCallback {

    private MainView view;
    private MainInteractor interactor;
    private Context context;

    public MainPresenterImpl(MainView view, MainInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void onSuccess(final String mainString) {
        view.hideRefreshing();
        Main main = new Gson().fromJson(mainString, Main.class);
        view.bindData(main);
        final ACache cache = ACache.get(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mainString != null) {
                    cache.put("main", mainString, 2 * ACache.TIME_DAY);
                }
            }
        }).start();
    }

    @Override
    public void onFailure(RetrofitError retrofitError) {
        view.hideRefreshing();
        switch (retrofitError.getKind()){
            case HTTP:
                RestError restError = (RestError) retrofitError.getBodyAs(RestError.class);
                if (restError != null) {
                    view.toastMessage(restError.message);
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
    public void loadDataFromNet() {
        interactor.getMain(this);
    }

    @Override
    public void loadDataFromCache() {
        final Handler handler = new Handler();
        final ACache cache = ACache.get(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String mainString = cache.getAsString("main");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mainString == null) {
                            loadDataFromNet();
                        } else {
                            Main main = new Gson().fromJson(mainString, Main.class);
                            view.bindData(main);
                        }
                    }
                });
            }
        }).start();

    }

}
