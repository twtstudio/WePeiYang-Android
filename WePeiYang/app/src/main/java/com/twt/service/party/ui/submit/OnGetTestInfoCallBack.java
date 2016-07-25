package com.twt.service.party.ui.submit;

import com.twt.service.party.bean.TestInfo;

import java.util.List;

/**
 * Created by dell on 2016/7/22.
 */
public interface OnGetTestInfoCallBack {
    void onGetTestInfo(List<TestInfo> tests);
    void onTestError(String msg);
    void onFailure();
}
