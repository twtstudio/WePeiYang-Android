package com.twtstudio.tjwhm.lostfound.mylist;

import com.twtstudio.tjwhm.lostfound.base.BaseBean;
import com.twtstudio.tjwhm.lostfound.base.CallbackBean;

/**
 * Created by tjwhm on 2017/7/7.
 **/

public interface MylistContract {

    public interface MylistView {
        void setMylistData(MylistBean mylistBean);

        void turnStatus(int id);

        void turnStatusSuccessCallBack();
    }

    public interface MylistPresenter {
        void setMylistData(MylistBean mylistBean);

        void loadMylistData(String lostOrFound, int page);

        void turnStatus(int id);

        void turnStatusSuccessCallBack(CallbackBean callbackBean);
    }
}
