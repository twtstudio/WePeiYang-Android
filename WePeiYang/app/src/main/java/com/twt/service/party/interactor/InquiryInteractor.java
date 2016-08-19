package com.twt.service.party.interactor;

import com.twt.service.party.ui.inquiry.Score20.OnGetScore20CallBack;
import com.twt.service.party.ui.inquiry.other.OnAppealCallBack;
import com.twt.service.party.ui.inquiry.other.OnGetOtherTestCallBack;

/**
 * Created by tjliqy on 2016/7/29.
 */

public interface InquiryInteractor {
    void load20TestInfo(OnGetScore20CallBack callBack);
    void loadOtherTestInfo(String type, OnGetOtherTestCallBack callBack);
    void appeal(String title, String content, String type, int testId, OnAppealCallBack callBack);
}
