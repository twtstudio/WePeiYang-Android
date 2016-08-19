package com.twt.service.bike.bike;

import com.twt.service.bike.common.IViewController;
import com.twt.service.bike.model.BikeStation;

/**
 * Created by jcy on 2016/8/11.
 */

public interface BikeViewController extends IViewController {
    void setStationDetail(BikeStation stationDetail);
}
