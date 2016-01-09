package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.News;
import com.twt.service.ui.news.details.OnGetNewsDetailsCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/11/16.
 */
public class NewsDetailsInteractorImpl implements NewsDetailsInteractor {
    @Override
    public void getNewsDetails(int index, final OnGetNewsDetailsCallback onGetNewsDetailsCallback) {
        ApiClient.getNewsDetails(index, new Callback<News>() {
            @Override
            public void success(News news, Response response) {
                onGetNewsDetailsCallback.onSuccess(news);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetNewsDetailsCallback.onFailure("无法连接到网络");
            }
        });
    }
}
