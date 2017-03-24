package com.twtstudio.tjliqy.party.ui.home;

import com.twtstudio.tjliqy.party.bean.StatusIdBean;
import com.twtstudio.tjliqy.party.bean.UserInfomation;

import java.util.List;

/**
 * Created by dell on 2016/7/19.
 */
public interface OnGetPersonalStatusCallBack {
    void onGetUserInfomation(UserInfomation infomation);
    void onGetStatusIds(List<StatusIdBean> ids);
    void onStatusError(String msg);
    void onFailure();
}
