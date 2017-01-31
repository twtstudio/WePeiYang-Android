package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.NewsList;
import com.twt.service.ui.news.associationsnews.OnGetAssociationCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/11/19.
 */
public class AssociationInteractorImpl implements AssociationInteractor {
    @Override
    public void getAssociationList(int page, final OnGetAssociationCallback onGetAssociationCallback) {
        ApiClient.getAssociationList(page, new Callback<NewsList>() {
            @Override
            public void success(NewsList newsList, Response response) {
                onGetAssociationCallback.onSuccess(newsList);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetAssociationCallback.onFailure(error);
            }
        });
    }
}
