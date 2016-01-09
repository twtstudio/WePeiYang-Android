package com.twt.service.interactor;

import com.twt.service.api.ApiClient;
import com.twt.service.bean.Main;
import com.twt.service.ui.main.OnGetMainCallback;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sunjuntao on 15/12/3.
 */
public class MainInteractorImpl implements MainInteractor {
    @Override
    public void getMain(final OnGetMainCallback onGetMainCallback) {
        ApiClient.getMain(new Callback<Main>() {
            @Override
            public void success(Main main, Response response) {
                onGetMainCallback.onSuccess(main);
            }

            @Override
            public void failure(RetrofitError error) {
                onGetMainCallback.onFailure("无法连接到网络");
            }
        });
    }
}
