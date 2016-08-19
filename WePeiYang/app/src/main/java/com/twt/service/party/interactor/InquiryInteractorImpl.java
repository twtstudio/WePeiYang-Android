package com.twt.service.party.interactor;


import android.util.Log;

import com.twt.service.party.api.ApiClient;
import com.twt.service.party.bean.Status;
import com.twt.service.party.ui.inquiry.Score20.OnGetScore20CallBack;
import com.twt.service.party.ui.inquiry.other.OnAppealCallBack;
import com.twt.service.party.ui.inquiry.other.OnGetOtherTestCallBack;
import com.twt.service.support.PrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tjliqy on 2016/7/29.
 */

public class InquiryInteractorImpl implements InquiryInteractor {

    @Override
    public void load20TestInfo(final OnGetScore20CallBack callBack) {
        ApiClient.loadStatus("20score", PrefUtils.getPrefUserNumber(), new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.body().getStatus() == 1){
                    callBack.onGetScoreInfo(response.body().getScore_info());
                }else {
                    callBack.onNoScoreInfo(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                callBack.onFailure();
            }
        });
    }

    @Override
    public void loadOtherTestInfo(String type, final OnGetOtherTestCallBack callBack) {
        ApiClient.loadStatus(type + "_gradecheck", "3013218130", new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.body().getStatus() == 1){
                    Log.d("lqy",response.body().toString());
                    callBack.onGetScoreInfo(response.body().getData().get(0));
                }else {
                    callBack.onNoScoreInfo(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }

    @Override
    public void appeal(String title, String content, String type, int testId, final OnAppealCallBack callBack) {
        ApiClient.appeal(PrefUtils.getPrefUserNumber(), type + "_shensu", testId, title, content, new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus() == 1){
                    callBack.onAppealSuccess(response.body().getMsg());
                }else {
                    callBack.onAppealFailure(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }
}
