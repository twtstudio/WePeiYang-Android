package com.twt.service.party.interactor;

import com.twt.service.party.api.ApiClient;
import com.twt.service.party.bean.Status;
import com.twt.service.party.ui.sign.OnGetTestCallBack;
import com.twt.service.party.ui.sign.OnSignTestCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dell on 2016/7/22.
 */
public class SignTestInteractorImpl implements SignTestInteractor{
    @Override
    public void loadTestInfo(String sno, final String testType, final OnGetTestCallBack callBack) {
        ApiClient.loadStatus(testType + "_entry", sno, new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.body().getStatus() == 1){
                    callBack.onGetTestInfo(response.body().getTest_info(), testType);
                } else{
                    callBack.onTestError(response.body().getMsg(), testType);
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                    callBack.onFailure();
            }
        });
    }

    @Override
    public void signTest(String sno, String testType, int testId, final OnSignTestCallBack callBack) {
        ApiClient.signTest(sno, testType+"_entry2", testId, new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus() == 1){
                    callBack.onSignSuccess(response.body().getMsg());
                }else {
                    callBack.onSignFailure(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }
}
