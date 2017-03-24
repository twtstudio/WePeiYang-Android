package com.twtstudio.tjliqy.party.interactor;

import com.twtstudio.tjliqy.party.ui.home.OnGetPersonalStatusCallBack;

/**
 * Created by dell on 2016/7/19.
 */
public interface PersonalStatusInteractor {
    void loadPersonalStatus(String sno, OnGetPersonalStatusCallBack callBack);
    void loadUserInformation(OnGetPersonalStatusCallBack callBack);
}
