package com.twtstudio.tjliqy.party.ui.inquiry.Score20;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.bean.Score20Info;
import com.twtstudio.tjliqy.party.interactor.InquiryInteractor;
import com.twtstudio.tjliqy.party.support.ResourceHelper;

import java.util.List;

/**
 * Created by tjliqy on 2016/7/29.
 */

public class Score20PresenterImpl implements Score20Presenter ,OnGetScore20CallBack{

    Score20View view;

    InquiryInteractor interactor;

    public Score20PresenterImpl(Score20View view, InquiryInteractor interactor){
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void getScoreInfo() {
        interactor.load20TestInfo(this);
    }

    @Override
    public void onGetScoreInfo(List<Score20Info> list) {
        view.bindData(list);
    }

    @Override
    public void onNoScoreInfo(String msg) {
        view.setErrorMsg(msg);
    }

    @Override
    public void onFailure() {
        view.setErrorMsg(ResourceHelper.getString(R.string.toast_network_failed));
    }
}
