package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.NewsList;
import com.twt.service.ui.news.collegenews.OnGetCollegeNewsCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/11/19.
 */
public class CollegeNewsInteractorImpl implements CollegeNewsInteractor {
    @Override
    public void getCollegeNewslist(int page, final OnGetCollegeNewsCallback onGetCollegeNewsCallback) {
        ApiClient.getCollegeNewslist(page, new Callback<NewsList>() {
            @Override
            public void success(NewsList newsList, Response response) {
                onGetCollegeNewsCallback.onSuccess(newsList);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetCollegeNewsCallback.onFailure(error);
            }
        });
    }
}
