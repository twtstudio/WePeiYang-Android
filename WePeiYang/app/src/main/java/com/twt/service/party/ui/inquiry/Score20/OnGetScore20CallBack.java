package com.twt.service.party.ui.inquiry.Score20;

import com.twt.service.party.bean.ScoreInfo;
import com.twt.service.party.bean.TestInfo;

import java.util.List;

/**
 * Created by tjliqy on 2016/7/29.
 */

public interface OnGetScore20CallBack {
    void onGetScoreInfo(List<ScoreInfo> list);
    void onNoScoreInfo(String msg);
    void onFailure();
}
