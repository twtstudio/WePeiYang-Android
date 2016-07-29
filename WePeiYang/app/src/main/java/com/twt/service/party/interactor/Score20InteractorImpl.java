package com.twt.service.party.interactor;


import com.twt.service.party.api.ApiClient;
import com.twt.service.party.bean.Status;
import com.twt.service.party.ui.inquiry.Score20.OnGetScore20CallBack;
import com.twt.service.support.PrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tjliqy on 2016/7/29.
 */

public class Score20InteractorImpl implements  Score20Interactor {

    @Override
    public void loadTestInfo(final OnGetScore20CallBack callBack) {
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
}
