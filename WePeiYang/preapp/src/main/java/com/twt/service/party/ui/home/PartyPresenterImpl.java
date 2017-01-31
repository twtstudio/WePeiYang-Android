package com.twt.service.party.ui.home;

import android.util.Log;

import com.twt.service.R;
import com.twt.service.party.bean.Status;
import com.twt.service.party.bean.StatusIdBean;
import com.twt.service.party.bean.UserInfomation;
import com.twt.service.party.interactor.PersonalStatusInteractor;
import com.twt.service.support.PrefUtils;
import com.twt.service.support.ResourceHelper;

import java.util.List;

/**
 * Created by dell on 2016/7/18.
 */
public class PartyPresenterImpl implements PartyPresenter, OnGetPersonalStatusCallBack {

    private PartyView view;

    private PersonalStatusInteractor interactor;

    public PartyPresenterImpl(PartyView view, PersonalStatusInteractor interactor){
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void loadUserInformation() {
        interactor.loadUserInformation(this);
    }

    @Override
    public void loadPersonalStatus() {
        interactor.loadPersonalStatus(PrefUtils.getPrefUserNumber(),this);
        Log.d("lqy","1"+PrefUtils.getPrefUserNumber());
    }

    @Override
    public void onGetUserInfomation(UserInfomation infomation) {
        PrefUtils.setPrefUserRealName(infomation.getRealname());
        PrefUtils.setPrefUserNumber(infomation.getStudentid());
        Log.d("lqy",PrefUtils.getPrefUserNumber());
        view.gotInformation();
    }

    @Override
    public void onGetStatusIds(List<StatusIdBean> ids) {
        view.bindData(ids);
//        view.toastMsg(ids.get(0).getMsg());
    }

    @Override
    public void onStatusError(String msg) {
        view.toastMsg(msg);
        view.setMsg(msg);
    }

    @Override
    public void onFailure() {
        view.toastMsg(ResourceHelper.getString(R.string.toast_network_failed));
        view.setMsg(ResourceHelper.getString(R.string.toast_network_failed));
    }
}
