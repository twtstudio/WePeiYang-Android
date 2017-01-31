package com.twt.service.party.interactor;

import com.twt.service.party.ui.sign.OnGetTestCallBack;
import com.twt.service.party.ui.sign.OnSignTestCallBack;

/**
 * Created by dell on 2016/7/22.
 */
public interface SignTestInteractor {
    void loadTestInfo(String sno, String testType, OnGetTestCallBack callBack);
    void signTest(String sno, String testType, int testId, OnSignTestCallBack callBack);
}
