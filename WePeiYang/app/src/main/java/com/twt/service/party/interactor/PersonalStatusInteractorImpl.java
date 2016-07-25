package com.twt.service.party.interactor;


import android.util.Log;

import com.twt.service.party.api.ApiClient;
import com.twt.service.party.bean.Status;
import com.twt.service.party.ui.home.OnGetPersonalStatusCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dell on 2016/7/19.
 */
public class PersonalStatusInteractorImpl implements PersonalStatusInteractor{

    @Override
    public void loadPersonalStatus(String sno, final OnGetPersonalStatusCallBack callBack) {
        ApiClient.loadStatus("personalstatus", sno, new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Log.e("lqy",response.body().toString());
                if(response.body().getStatus() == 1){
                    callBack.onGetStatusIds(response.body().getStatus_id());
                } else{
                 callBack.onStatusError(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.e("lqy","error");
                callBack.onFailure();
            }
        });
    }
}
