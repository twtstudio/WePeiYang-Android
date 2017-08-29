package com.twtstudio.service.classroom.model;

import android.content.Context;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.service.classroom.view.MainActivity;
import com.twtstudio.service.classroom.view.MainActivityViewModel;
import com.twtstudio.service.classroom.view.OnLoadMoreItemViewModel;

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

    public void showHttpErrorOnMainActivity(MainActivityViewModel viewModel, Throwable throwable) {
        if (throwable instanceof IOException || throwable instanceof HttpException) {
            viewModel.isHttpError.set(true);
            viewModel.isLoading.set(true);
            viewModel.isLoading.set(false);
        }
    }

    public void stopLoading(MainActivityViewModel viewModel) {
        viewModel.onLoadMoreError = true;
        if (!viewModel.items.isEmpty() && viewModel.items.size() > 6) {
            viewModel.items.remove(viewModel.items.size() - 1);
            viewModel.items.add(new OnLoadMoreItemViewModel(false, true));
        }
    }
}
