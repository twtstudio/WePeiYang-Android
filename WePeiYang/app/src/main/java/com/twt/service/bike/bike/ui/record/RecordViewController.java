package com.twt.service.bike.bike.ui.record;

import com.twt.service.bike.common.IViewController;
import com.twt.service.bike.model.BikeRecord;

import java.util.List;

/**
 * Created by jcy on 2016/9/9.
 */
public interface RecordViewController extends IViewController {
    void refreshItems(List<BikeRecord> recordList);
}
