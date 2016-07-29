package com.twt.service.party.ui.inquiry.Score20;

import com.twt.service.party.bean.ScoreInfo;

import java.util.List;

/**
 * Created by tjliqy on 2016/7/29.
 */

public interface Score20View {
    void toastMsg(String msg);
    void bindData(List<ScoreInfo> list);
    void setErrorMsg(String errorMsg);
}
