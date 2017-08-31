package com.twtstudio.tjwhm.lostfound.release;

import com.twtstudio.tjwhm.lostfound.base.BaseBean;
import com.twtstudio.tjwhm.lostfound.base.BaseContract;
import com.twtstudio.tjwhm.lostfound.base.CallbackBean;
import com.twtstudio.tjwhm.lostfound.detail.DetailBean;

import java.io.File;
import java.util.Map;

/**
 * Created by tjwhm on 2017/7/6.
 **/

public interface ReleaseContract {
    public interface ReleaseView extends BaseContract.BaseView {
        void successCallBack(BaseBean baseBean);

        void setEditData(DetailBean detailBean);

        void deleteSuccessCallBack();

        void drawRecyclerView(int position);

        void onTypeItemSelected(int position);

    }

    public interface ReleasePresenter extends BaseContract.BasePresenter {


        void uploadReleaseData(Map<String, Object> map, String lostOrFound);

        void uploadReleaseDataWithPic
                (Map<String, Object> map, String lostOrFound, File file);

        void successCallBack(BaseBean baseBean);


        void successEditCallback(BaseBean baseBean);

        void delete(int id);

        void deleteSuccessCallBack(BaseBean baseBean);

        void uploadEditDataWithPic(Map<String, Object> map, String lostOrFound, File file, int id);
    }

}
