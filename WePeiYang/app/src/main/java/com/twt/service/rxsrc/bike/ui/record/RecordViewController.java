package com.twt.service.rxsrc.bike.ui.record;

import com.twt.service.rxsrc.common.IViewController;
import com.twt.service.rxsrc.model.BikeRecord;

import java.util.List;

/**
 * Created by jcy on 2016/9/9.
 */
public interface RecordViewController extends IViewController {
    void refreshItems(List<BikeRecord> recordList);
}
