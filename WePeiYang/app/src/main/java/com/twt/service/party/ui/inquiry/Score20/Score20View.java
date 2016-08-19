package com.twt.service.party.ui.inquiry.Score20;

import com.twt.service.party.bean.Score20Info;

import java.util.List;

/**
 * Created by tjliqy on 2016/7/29.
 */

public interface Score20View {
    void toastMsg(String msg);
    void bindData(List<Score20Info> list);
    void setErrorMsg(String errorMsg);
}
