package com.twtstudio.tjwhm.lostfound.mylist;

/**
 * Created by tjwhm on 2017/7/7.
 **/

public interface MylistContract {

    public interface MylistView {
        void setMylistData(MylistBean mylistBean);
    }

    public interface MylistPresenter {
        void setMylistData(MylistBean mylistBean);

        void loadMylistData(String lostOrFound, int page);
    }
}
