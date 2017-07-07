package com.twtstudio.tjwhm.lostfound.release;

import com.twtstudio.tjwhm.lostfound.base.BaseBean;
import com.twtstudio.tjwhm.lostfound.base.BaseContract;
import com.twtstudio.tjwhm.lostfound.detail.DetailBean;

import java.util.Map;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public interface ReleaseContract {
    public interface ReleaseView extends BaseContract.BaseView {
        void successCallBack();

        void setEditData(DetailBean detailBean);

        void deleteSuccessCallBack();

    }

    public interface ReleasePresenter extends BaseContract.BasePresenter {


        void updateReleaseData(Map<String, Object> map, String lostOrFound);

        void successCallBack(BaseBean baseBean);

        void updateEditData(Map<String, Object> map, String lostOrFound, int id);

        void successEditCallback(BaseBean baseBean);

        void delete(int id);

        void deleteSuccessCallBack(BaseBean baseBean);
    }

}
