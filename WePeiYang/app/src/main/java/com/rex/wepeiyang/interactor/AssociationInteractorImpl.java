package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.api.ApiClient;
import com.rex.wepeiyang.bean.NewsList;
import com.rex.wepeiyang.ui.news.associationsnews.OnGetAssociationCallback;

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
                onGetAssociationCallback.onFailure("无法连接到网络");
            }
        });
    }
}
