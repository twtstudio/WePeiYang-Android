package com.twt.service.bike.bike.bikeAuth;


import com.twt.service.bike.common.IViewController;
import com.twt.service.bike.model.BikeAuth;

/**
 * Created by jcy on 2016/8/7.
 */

public interface BikeAuthController extends IViewController {
    void onTokenGot(BikeAuth bikeAuth);
}
