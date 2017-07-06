package com.twtstudio.tjwhm.lostfound.release;

import com.twtstudio.tjwhm.lostfound.base.BaseContract;

import java.util.Map;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public interface ReleaseContract {
    public interface ReleaseView extends BaseContract.BaseView {
        void successCallBack();
        void turnToAuth();
    }

    public interface ReleasePresenter extends BaseContract.BasePresenter {
        void updateReleaseData(Map<String, Object> map);
    }

}
