package com.twt.service.party.ui.sign;

import android.view.View;

import com.twt.service.R;
import com.twt.service.party.bean.TestInfo;
import com.twt.service.party.interactor.SignTestInteractor;
import com.twt.service.support.PrefUtils;
import com.twt.service.support.ResourceHelper;

import java.util.List;

/**
 * Created by tjliqy on 2016/7/29.
 */

public class SignPresenterImpl implements SignPresenter, OnGetTestCallBack{

    public static final String TYPE_APPLICANT = "applicant";

    public static final String TYPE_ACADEMY = "academy";

    public static final String TYPE_PROBATIONARY = "probationary";

    private SignView view;

    private SignTestInteractor interactor;

    SignPresenterImpl(SignView view, SignTestInteractor interactor){
        this.view = view;
        this.interactor = interactor;
    }
    @Override
    public void getTest() {
        interactor.loadTestInfo(PrefUtils.getPrefUserNumber(), TYPE_APPLICANT, this);
        interactor.loadTestInfo(PrefUtils.getPrefUserNumber(), TYPE_ACADEMY, this);
        interactor.loadTestInfo(PrefUtils.getPrefUserNumber(), TYPE_PROBATIONARY, this);
    }

    @Override
    public void onGetTestInfo(TestInfo test, String type) {
        view.bindData(test, type);
    }

    @Override
    public void onTestError(String msg,String type) {
        view.setNoTestReason(msg, type);
    }

    @Override
    public void onFailure() {
        view.toastMsg(ResourceHelper.getString(R.string.toast_network_failed));
    }
}
