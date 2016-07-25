package com.twt.service.party.interactor;

import com.twt.service.party.ui.home.OnGetPersonalStatusCallBack;

/**
 * Created by dell on 2016/7/19.
 */
public interface PersonalStatusInteractor {
    void loadPersonalStatus(String sno, OnGetPersonalStatusCallBack callBack);
}
