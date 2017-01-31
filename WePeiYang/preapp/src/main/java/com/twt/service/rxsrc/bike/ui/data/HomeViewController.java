package com.twt.service.rxsrc.bike.ui.data;


import com.twt.service.rxsrc.common.IViewController;
import com.twt.service.rxsrc.model.BikeUserInfo;

/**
 * Created by jcy on 2016/8/9.
 */

public interface HomeViewController extends IViewController {
    void setBikeUserInfo(BikeUserInfo bikeUserInfo);
}
