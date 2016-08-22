package com.twt.service.party.interactor;

import com.twt.service.party.api.ApiClient;
import com.twt.service.party.bean.Status;
import com.twt.service.party.ui.submit.detail.OnSubmitCallBack;
import com.twt.service.support.PrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tjliqy on 2016/8/22.
 */
public class SubmitInteractorImpl implements SubmitInteractor {
    @Override
    public void submit(String title, String content, String type, OnSubmitCallBack callBack) {
        ApiClient.submit(PrefUtils.getPrefUserNumber(), title, content, type, new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus() == 1){
                    callBack.onSuccess(response.body().getMessage());
                }else {
                    callBack.onError(response.body().getMsg() );
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                callBack.onFailure();
            }
        });
    }
}
