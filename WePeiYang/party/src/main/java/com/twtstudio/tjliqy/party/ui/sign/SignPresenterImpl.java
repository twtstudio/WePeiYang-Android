package com.twtstudio.tjliqy.party.ui.sign;

import android.util.Log;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.bean.TestInfo;
import com.twtstudio.tjliqy.party.interactor.SignTestInteractor;
import com.twtstudio.tjliqy.party.support.PrefUtils;
import com.twtstudio.tjliqy.party.support.ResourceHelper;

/**
 * Created by tjliqy on 2016/7/29.
 */

public class SignPresenterImpl implements SignPresenter, OnGetTestCallBack, OnSignTestCallBack{

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
    public void signTest(String type,int testId) {
        Log.d("lqy",testId+"presenter");
        interactor.signTest(PrefUtils.getPrefUserNumber(),type,testId,this);
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

    @Override
    public void onSignSuccess(String msg) {
        view.toastMsg(msg);
    }

    @Override
    public void onSignFailure(String msg) {
        view.toastMsg(msg);
    }
}
