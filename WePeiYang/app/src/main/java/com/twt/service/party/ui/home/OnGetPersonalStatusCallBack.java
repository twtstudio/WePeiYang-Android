package com.twt.service.party.ui.home;

import com.twt.service.party.bean.Status;
import com.twt.service.party.bean.StatusIdBean;

import java.util.List;

/**
 * Created by dell on 2016/7/19.
 */
public interface OnGetPersonalStatusCallBack {
    void onGetStatusIds(List<StatusIdBean> ids);
    void onStatusError(String msg);
    void onFailure();
}
