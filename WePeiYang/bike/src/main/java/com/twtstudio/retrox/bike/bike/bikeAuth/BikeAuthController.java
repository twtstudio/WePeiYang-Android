package com.twtstudio.retrox.bike.bike.bikeAuth;


import com.twtstudio.retrox.bike.common.IViewController;
import com.twtstudio.retrox.bike.model.BikeAuth;

/**
 * Created by jcy on 2016/8/7.
 */

public interface BikeAuthController extends IViewController {
    void onTokenGot(BikeAuth bikeAuth);

}
