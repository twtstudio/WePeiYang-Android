package com.twt.service.rxsrc.bike;

import com.twt.service.rxsrc.common.IViewController;
import com.twt.service.rxsrc.model.BikeStation;

/**
 * Created by jcy on 2016/8/11.
 */

public interface BikeViewController extends IViewController {
    void setStationDetail(BikeStation stationDetail);
}
