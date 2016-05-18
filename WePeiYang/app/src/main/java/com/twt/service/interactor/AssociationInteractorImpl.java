package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.NewsList;
import com.twt.service.ui.news.associationsnews.OnGetAssociationCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sunjuntao on 15/11/19.
 */
public class AssociationInteractorImpl implements AssociationInteractor {
    @Override
    public void getAssociationList(int page, final OnGetAssociationCallback onGetAssociationCallback) {
        ApiClient.getAssociationList(page, new Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                onGetAssociationCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {
                onGetAssociationCallback.onFailure(t);
            }
        });
    }
}
