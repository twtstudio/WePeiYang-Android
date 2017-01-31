package com.twt.service.ui.account;

import com.twt.service.bean.RestError;
import com.twt.service.interactor.AccountInteractor;
import com.twt.service.support.PrefUtils;

import retrofit.RetrofitError;

/**
 * Created by sunjuntao on 16/1/27.
 */
public class AccountPresenterImpl implements AccountPresenter, UnBindTjuCallback {

    private AccountView view;
    private AccountInteractor interactor;

    public AccountPresenterImpl(AccountView view, AccountInteractor interactor){
        this.view = view;
        this.interactor = interactor;
    }
    @Override
    public void unbindTju() {
        interactor.unbind(PrefUtils.getToken(), PrefUtils.getUsername());
    }

    @Override
    public void onSuccess() {
        view.toastMessage("解绑成功");
    }

    @Override
    public void onFailure(RetrofitError retrofitError) {
        RestError error = (RestError)retrofitError.getBodyAs(RestError.class);
        if (error == null){
            view.toastMessage("无法连接到网络");
        }else {
            view.toastMessage(error.message);
        }
    }
}
