package com.twtstudio.tjliqy.party.ui.home;

import com.twtstudio.tjliqy.party.bean.StatusIdBean;

import java.util.List;

/**
 * Created by dell on 2016/7/18.
 */
public interface PartyView {
    void gotInformation();
    void bindData(List<StatusIdBean> ids);
    void toastMsg(String msg);
    void setMsg(String msg);
}
