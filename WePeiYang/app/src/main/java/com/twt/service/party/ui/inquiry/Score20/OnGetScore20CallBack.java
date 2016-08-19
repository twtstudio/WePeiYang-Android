package com.twt.service.party.ui.inquiry.Score20;

import com.twt.service.party.bean.Score20Info;

import java.util.List;

/**
 * Created by tjliqy on 2016/7/29.
 */

public interface OnGetScore20CallBack {
    void onGetScoreInfo(List<Score20Info> list);
    void onNoScoreInfo(String msg);
    void onFailure();
}
