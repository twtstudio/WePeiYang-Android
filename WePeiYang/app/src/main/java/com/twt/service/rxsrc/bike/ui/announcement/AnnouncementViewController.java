package com.twt.service.rxsrc.bike.ui.announcement;

import com.twt.service.rxsrc.common.IViewController;
import com.twt.service.rxsrc.model.BikeAnnouncement;

import java.util.List;

/**
 * Created by jcy on 16-8-20.
 */
public interface AnnouncementViewController extends IViewController{
    void setAnnouncementList(List<BikeAnnouncement> announcementList);
}
