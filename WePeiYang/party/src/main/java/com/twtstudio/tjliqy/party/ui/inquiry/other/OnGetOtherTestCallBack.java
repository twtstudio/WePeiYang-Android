package com.twtstudio.tjliqy.party.ui.inquiry.other;

import com.twtstudio.tjliqy.party.bean.OtherScoreInfo;

/**
 * Created by tjliqy on 2016/8/16.
 */
public interface OnGetOtherTestCallBack {
    void onGetScoreInfo(OtherScoreInfo scoreInfo);
    void onNoScoreInfo(String msg);
    void onOtherScoreFailure();
}
