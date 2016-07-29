package com.twt.service.party.ui.inquiry.Score20;

import com.twt.service.R;
import com.twt.service.party.bean.ScoreInfo;
import com.twt.service.party.interactor.Score20Interactor;
import com.twt.service.support.ResourceHelper;

import java.util.List;

/**
 * Created by tjliqy on 2016/7/29.
 */

public class Score20PresenterImpl implements Score20Presenter ,OnGetScore20CallBack{

    Score20View view;

    Score20Interactor interactor;

    public Score20PresenterImpl(Score20View view, Score20Interactor interactor){
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void getScoreInfo() {
        interactor.loadTestInfo(this);
    }

    @Override
    public void onGetScoreInfo(List<ScoreInfo> list) {
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
