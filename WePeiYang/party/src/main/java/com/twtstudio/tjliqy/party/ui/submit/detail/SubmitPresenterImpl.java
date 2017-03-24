package com.twtstudio.tjliqy.party.ui.submit.detail;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.interactor.SubmitInteractor;
import com.twtstudio.tjliqy.party.support.ResourceHelper;

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
