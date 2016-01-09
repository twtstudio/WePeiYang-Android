package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.NewsList;
import com.twt.service.ui.news.importantnews.OnGetImportantNewsCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/11/14.
 */
public class ImportantNewsInteractorImpl implements ImportantNewsInteractor {
    @Override
    public void getImportantNews(int page, final OnGetImportantNewsCallback onGetImportantNewsCallback) {
        ApiClient.getImportantNewsList(page, new Callback<NewsList>() {
            @Override
            public void success(NewsList newsList, Response response) {
                onGetImportantNewsCallback.onSuccess(newsList);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetImportantNewsCallback.onFailure("无法连接到网络");
            }
        });
    }
}
