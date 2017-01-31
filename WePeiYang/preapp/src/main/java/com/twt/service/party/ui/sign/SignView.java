package com.twt.service.party.ui.sign;

import com.twt.service.party.bean.TestInfo;

/**
 * Created by tjliqy on 2016/7/29.
 */

public interface SignView {
    void toastMsg(String msg);
    void bindData(TestInfo testInfo, String type);
    void setNoTestReason(String msg, String type);
}
