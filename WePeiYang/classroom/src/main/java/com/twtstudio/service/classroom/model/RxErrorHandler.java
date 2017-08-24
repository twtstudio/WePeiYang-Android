package com.twtstudio.service.classroom.model;

import android.content.Context;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.service.classroom.view.MainActivityViewModel;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by DefaultAccount on 2017/8/24.
 */

public class RxErrorHandler extends com.twt.wepeiyang.commons.network.RxErrorHandler {
    private Context mContext;

    public RxErrorHandler(Context context) {
        mContext = context;
    }
    public void showHttpErrorOnMainActivity(MainActivityViewModel viewModel, Throwable throwable){
        if(throwable instanceof IOException || throwable instanceof HttpException) {
            viewModel.isHttpError.set(true);
            viewModel.isLoading.set(true);
            viewModel.isLoading.set(false);
        }
    }
}
