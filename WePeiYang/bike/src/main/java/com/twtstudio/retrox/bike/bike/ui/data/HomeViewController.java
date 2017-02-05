package com.twtstudio.retrox.bike.bike.ui.data;


import com.twtstudio.retrox.bike.common.IViewController;
import com.twtstudio.retrox.bike.model.BikeUserInfo;

/**
 * Created by jcy on 2016/8/9.
 */

public interface HomeViewController extends IViewController {
    void setBikeUserInfo(BikeUserInfo bikeUserInfo);
}
