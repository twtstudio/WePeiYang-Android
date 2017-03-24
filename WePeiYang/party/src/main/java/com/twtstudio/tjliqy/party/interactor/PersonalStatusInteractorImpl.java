package com.twtstudio.tjliqy.party.interactor;


import com.twtstudio.tjliqy.party.api.ApiClient;
import com.twtstudio.tjliqy.party.bean.Status;
import com.twtstudio.tjliqy.party.bean.UserInfomation;
import com.twtstudio.tjliqy.party.ui.home.OnGetPersonalStatusCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dell on 2016/7/19.
 */
public class PersonalStatusInteractorImpl implements PersonalStatusInteractor{

    @Override
    public void loadUserInformation(final OnGetPersonalStatusCallBack callBack) {
        ApiClient.loadUserInfomation(new Callback<UserInfomation>() {
            @Override
            public void onResponse(Call<UserInfomation> call, Response<UserInfomation> response) {
                callBack.onGetUserInfomation(response.body());
            }

            @Override
            public void onFailure(Call<UserInfomation> call, Throwable t) {
                callBack.onFailure();
            }
        });
    }

    @Override
    public void loadPersonalStatus(String sno, final OnGetPersonalStatusCallBack callBack) {
        ApiClient.loadStatus("personalstatus", sno, new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.body().getStatus() == 1){
                    callBack.onGetStatusIds(response.body().getStatus_id());
                } else{
                 callBack.onStatusError(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                callBack.onFailure();
            }
        });
    }
}
