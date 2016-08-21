package com.twt.service.bike.bike.ui.announcement;

import com.twt.service.bike.common.IViewController;
import com.twt.service.bike.model.BikeAnnouncement;

import java.util.List;

/**
 * Created by jcy on 16-8-20.
 */
public interface AnnouncementViewController extends IViewController{
    void setAnnouncementList(List<BikeAnnouncement> announcementList);
}
