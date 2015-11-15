package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.api.ApiClient;
import com.rex.wepeiyang.bean.NewsList;
import com.rex.wepeiyang.ui.news.importantnews.OnGetImportantNewsCallback;

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
                onGetImportantNewsCallback.onFailure(error.toString());
            }
        });
    }
}
