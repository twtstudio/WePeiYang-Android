package com.twtstudio.tjwhm.lostfound.search;

import com.twtstudio.tjwhm.lostfound.base.BaseContract;
import com.twtstudio.tjwhm.lostfound.waterfall.WaterfallBean;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public interface SearchContract {
    public interface SearchUIView extends BaseContract.BaseView {
        void setSearchData(WaterfallBean waterfallBean);
    }

    public interface SearchPresenter extends BaseContract.BasePresenter {
        void loadSearchData(String keyword,int page);
        void setSearchData(WaterfallBean waterfallBean);
    }
}
