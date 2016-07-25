package com.twt.service.party.ui.home;

import com.twt.service.party.bean.Status;
import com.twt.service.party.bean.StatusIdBean;
import com.twt.service.party.interactor.PersonalStatusInteractor;
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
    public void loadPersonalStatus() {
        interactor.loadPersonalStatus("3015218062",this);
    }

    @Override
    public void onGetStatusIds(List<StatusIdBean> ids) {
        view.bindData(ids);
        view.toastMsg(ids.get(0).getMsg());
    }

    @Override
    public void onStatusError(String msg) {
        view.toastMsg(msg);
    }

    @Override
    public void onFailure() {
        view.toastMsg("网络错误");
    }
}
