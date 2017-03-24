package com.twtstudio.tjliqy.party.ui.inquiry.other;

import com.twtstudio.tjliqy.party.bean.OtherScoreInfo;

/**
 * Created by tjliqy on 2016/8/17.
 */
public interface OtherView {
    void bindData(OtherScoreInfo scoreInfo);
    void toastMsg(String msg);
    void setErrorMsg(String errorMsg);
}
