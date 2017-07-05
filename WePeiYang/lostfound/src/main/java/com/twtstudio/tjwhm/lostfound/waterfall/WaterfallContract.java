package com.twtstudio.tjwhm.lostfound.waterfall;

import com.twtstudio.tjwhm.lostfound.base.BaseContract;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public interface WaterfallContract {
    public interface WaterfallView extends BaseContract.BaseView {
        void setWaterfallData(WaterfallBean waterfallBean);
    }

    public interface WaterfallPresenter extends BaseContract.BasePresenter {
        void setWaterfallData(WaterfallBean waterfallBean);
        void loadWaterfallData(String lostOrFound,int page);
    }
}
