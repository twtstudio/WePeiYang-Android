package com.twtstudio.tjliqy.party.ui.inquiry.Score20;

import com.twtstudio.tjliqy.party.bean.Score20Info;

import java.util.List;

/**
 * Created by tjliqy on 2016/7/29.
 */

public interface Score20View {
    void toastMsg(String msg);
    void bindData(List<Score20Info> list);
    void setErrorMsg(String errorMsg);
}
