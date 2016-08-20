package com.twt.service.party.ui.home;

import com.twt.service.party.bean.Status;
import com.twt.service.party.bean.StatusIdBean;

import java.util.List;

/**
 * Created by dell on 2016/7/18.
 */
public interface PartyView {
    void startBindActivity();
    void startLoginActivity();
    void gotInformation();
    void bindData(List<StatusIdBean> ids);
    void toastMsg(String msg);
    void setMsg(String msg);
}
