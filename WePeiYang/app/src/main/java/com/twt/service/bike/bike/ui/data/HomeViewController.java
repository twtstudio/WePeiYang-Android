package com.twt.service.bike.bike.ui.data;


import com.twt.service.bike.common.IViewController;
import com.twt.service.bike.model.BikeUserInfo;

/**
 * Created by jcy on 2016/8/9.
 */

public interface HomeViewController extends IViewController {
    void setBikeUserInfo(BikeUserInfo bikeUserInfo);
}
