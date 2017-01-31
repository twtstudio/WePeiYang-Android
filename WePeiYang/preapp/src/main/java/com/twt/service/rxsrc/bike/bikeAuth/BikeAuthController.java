package com.twt.service.rxsrc.bike.bikeAuth;


import com.twt.service.rxsrc.common.IViewController;
import com.twt.service.rxsrc.model.BikeAuth;

/**
 * Created by jcy on 2016/8/7.
 */

public interface BikeAuthController extends IViewController {
    void onTokenGot(BikeAuth bikeAuth);

}
