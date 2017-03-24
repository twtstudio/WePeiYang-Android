package com.twtstudio.tjliqy.party.ui.inquiry.other;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.bean.OtherScoreInfo;
import com.twtstudio.tjliqy.party.interactor.InquiryInteractor;
import com.twtstudio.tjliqy.party.support.ResourceHelper;
import com.twtstudio.tjliqy.party.ui.inquiry.appeal.ApealView;

/**
 * Created by tjliqy on 2016/8/16.
 */
public class OtherPresenterImpl implements OtherPresenter,OnGetOtherTestCallBack,OnAppealCallBack{

    private OtherView view;

    private ApealView apealView;

    private InquiryInteractor interactor;

    public OtherPresenterImpl(OtherView view, InquiryInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public OtherPresenterImpl(ApealView view,InquiryInteractor interactor){
        apealView = view;
        this.interactor = interactor;
    }

    @Override
    public void getGrade(String type) {
        interactor.loadOtherTestInfo(type,this);
    }

    @Override
    public void appeal(String title, String content, String type, int testId) {
        interactor.appeal(title,content,type,testId,this);
    }

    @Override
    public void onGetScoreInfo(OtherScoreInfo scoreInfo) {
        if (scoreInfo.getEntry_status() != null) {
            switch (scoreInfo.getEntry_status()){
                case "1":
                    scoreInfo.setEntry_status("正常");
                    break;
                case "2":
                    scoreInfo.setEntry_status("作弊");
                    break;
                case "3":
                    scoreInfo.setEntry_status("违纪");
                    break;
                case "4":
                    scoreInfo.setEntry_status("缺考");
                    break;
                default:
                    break;
            }
        }
        if (scoreInfo.getEntry_ispassed() != null) {
            switch (scoreInfo.getEntry_ispassed()){
                case "0":
                    scoreInfo.setEntry_ispassed("不合格");
                    break;
                case "1":
                    scoreInfo.setEntry_ispassed("合格");
                    break;
                case "2":
                    scoreInfo.setEntry_ispassed("优秀");
                    break;
                default:
                    break;
            }
        }
        if (scoreInfo.getEntry_isallpassed() != null) {
            switch (scoreInfo.getEntry_isallpassed()){
                case "1":
                    scoreInfo.setEntry_isallpassed("通过");
                    break;
                default:
                    scoreInfo.setEntry_isallpassed("未通过");
                    break;
            }

        }

        view.bindData(scoreInfo);
    }

    @Override
    public void onNoScoreInfo(String msg) {
        view.setErrorMsg(msg);
    }

    @Override
    public void onOtherScoreFailure() {
        view.setErrorMsg(ResourceHelper.getString(R.string.toast_network_failed));
    }

    @Override
    public void onAppealSuccess(String msg) {
        apealView.appealSuccess(msg);
    }

    @Override
    public void onAppealFailure(String msg) {
        apealView.appealFailure(msg);
    }
}
