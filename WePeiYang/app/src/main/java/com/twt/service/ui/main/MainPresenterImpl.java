package com.twt.service.ui.main;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.twt.service.bean.Main;
import com.twt.service.bean.RestError;
import com.twt.service.interactor.MainInteractor;
import com.twt.service.support.ACache;
import com.twt.service.support.FileCacheLoader;

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
        FileCacheLoader loader = FileCacheLoader.build();
        loader.setMain(mainString);
    }

    @Override
    public void onFailure(RetrofitError retrofitError) {
        view.hideRefreshing();
        switch (retrofitError.getKind()) {
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
        FileCacheLoader loader = FileCacheLoader.build();
        loader.getMain(this, view);
    }

}
