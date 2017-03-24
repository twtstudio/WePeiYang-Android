package com.twtstudio.tjliqy.party.interactor;

import com.twtstudio.tjliqy.party.ui.inquiry.Score20.OnGetScore20CallBack;
import com.twtstudio.tjliqy.party.ui.inquiry.other.OnAppealCallBack;
import com.twtstudio.tjliqy.party.ui.inquiry.other.OnGetOtherTestCallBack;

/**
 * Created by tjliqy on 2016/7/29.
 */

public interface InquiryInteractor {
    void load20TestInfo(OnGetScore20CallBack callBack);
    void loadOtherTestInfo(String type, OnGetOtherTestCallBack callBack);
    void appeal(String title, String content, String type, int testId, OnAppealCallBack callBack);
}
