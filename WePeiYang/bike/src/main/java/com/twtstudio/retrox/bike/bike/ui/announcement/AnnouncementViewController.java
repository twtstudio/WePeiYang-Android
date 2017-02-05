package com.twtstudio.retrox.bike.bike.ui.announcement;


import com.twtstudio.retrox.bike.common.IViewController;
import com.twtstudio.retrox.bike.model.BikeAnnouncement;

import java.util.List;

/**
 * Created by jcy on 16-8-20.
 */
public interface AnnouncementViewController extends IViewController {
    void setAnnouncementList(List<BikeAnnouncement> announcementList);
}
