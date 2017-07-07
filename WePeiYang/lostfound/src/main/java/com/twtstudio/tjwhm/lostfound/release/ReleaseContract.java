package com.twtstudio.tjwhm.lostfound.release;

import com.twtstudio.tjwhm.lostfound.base.BaseBean;
import com.twtstudio.tjwhm.lostfound.base.BaseContract;

import java.util.Map;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public interface ReleaseContract {
    public interface ReleaseView extends BaseContract.BaseView {
        void successCallBack();
    }

    public interface ReleasePresenter extends BaseContract.BasePresenter {
        void updateReleaseData(Map<String, Object> map,String lostOrFound);
        void successCallBack(BaseBean baseBean);
    }

}
