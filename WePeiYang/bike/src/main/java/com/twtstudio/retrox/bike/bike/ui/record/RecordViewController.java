package com.twtstudio.retrox.bike.bike.ui.record;


import com.twtstudio.retrox.bike.common.IViewController;
import com.twtstudio.retrox.bike.model.BikeRecord;

import java.util.List;

/**
 * Created by jcy on 2016/9/9.
 */
public interface RecordViewController extends IViewController {
    void refreshItems(List<BikeRecord> recordList);
}
