package com.twtstudio.retrox.bike.bike;


import com.twtstudio.retrox.bike.common.IViewController;
import com.twtstudio.retrox.bike.model.BikeStation;

/**
 * Created by jcy on 2016/8/11.
 */

public interface BikeViewController extends IViewController {
    void setStationDetail(BikeStation stationDetail);
}
