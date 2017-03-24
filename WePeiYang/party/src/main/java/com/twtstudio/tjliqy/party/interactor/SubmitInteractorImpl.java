package com.twtstudio.tjliqy.party.interactor;

import com.twtstudio.tjliqy.party.api.ApiClient;
import com.twtstudio.tjliqy.party.bean.Status;
import com.twtstudio.tjliqy.party.support.PrefUtils;
import com.twtstudio.tjliqy.party.ui.submit.detail.OnSubmitCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tjliqy on 2016/8/22.
 */
public class SubmitInteractorImpl implements SubmitInteractor {
    @Override
    public void submit(String title, String content, String type, final OnSubmitCallBack callBack) {
        ApiClient.submit(PrefUtils.getPrefUserNumber(), title, content, type, new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus() == 1){
                    callBack.onSuccess(response.body().getMsg());
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
