package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.NewsList;
import com.twt.service.ui.news.viewpoint.OnGetViewPointCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/11/18.
 */
public class ViewPointInteractorImpl implements ViewPointInteractor {
    @Override
    public void getViewPoints(int page, final OnGetViewPointCallback onGetViewPointCallback) {
        ApiClient.getViewPointList(page, new Callback<NewsList>() {
            @Override
            public void success(NewsList newsList, Response response) {
                onGetViewPointCallback.onSuccess(newsList);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetViewPointCallback.onFailure("无法连接到网络");
            }
        });
    }
}
