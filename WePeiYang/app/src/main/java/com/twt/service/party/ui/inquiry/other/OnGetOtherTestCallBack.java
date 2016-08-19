package com.twt.service.party.ui.inquiry.other;

import com.twt.service.party.bean.OtherScoreInfo;

/**
 * Created by tjliqy on 2016/8/16.
 */
public interface OnGetOtherTestCallBack {
    void onGetScoreInfo(OtherScoreInfo scoreInfo);
    void onNoScoreInfo(String msg);
}
