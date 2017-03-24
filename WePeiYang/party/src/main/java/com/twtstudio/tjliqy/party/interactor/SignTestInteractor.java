package com.twtstudio.tjliqy.party.interactor;

import com.twtstudio.tjliqy.party.ui.sign.OnGetTestCallBack;
import com.twtstudio.tjliqy.party.ui.sign.OnSignTestCallBack;

/**
 * Created by dell on 2016/7/22.
 */
public interface SignTestInteractor {
    void loadTestInfo(String sno, String testType, OnGetTestCallBack callBack);
    void signTest(String sno, String testType, int testId, OnSignTestCallBack callBack);
}
