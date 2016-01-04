package com.rex.wepeiyang.interactor;

import com.rex.wepeiyang.api.ApiClient;
import com.rex.wepeiyang.bean.Main;
import com.rex.wepeiyang.ui.main.OnGetMainCallback;

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
