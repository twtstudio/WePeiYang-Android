package com.twt.service.party.ui.submit.detail;

import com.twt.service.R;
import com.twt.service.party.interactor.SubmitInteractor;
import com.twt.service.support.ResourceHelper;

/**
 * Created by tjliqy on 2016/8/22.
 */
public class SubmitPresenterImpl implements SubmitPresenter,OnSubmitCallBack{

    SubmitInteractor interactor;
    SubmitDetailView view;
    public SubmitPresenterImpl(SubmitDetailView view, SubmitInteractor interactor) {
        this.interactor = interactor;
        this.view = view;
    }

    @Override
    public void submit(String title, String content, String type) {
        interactor.submit(title,content,type,this);
    }

    @Override
    public void onSuccess(String msg) {
        view.onSuccess(msg);
    }

    @Override
    public void onError(String msg) {
        view.toastMsg(msg);
    }

    @Override
    public void onFailure() {
        view.toastMsg(ResourceHelper.getString(R.string.toast_network_failed));
    }

}
