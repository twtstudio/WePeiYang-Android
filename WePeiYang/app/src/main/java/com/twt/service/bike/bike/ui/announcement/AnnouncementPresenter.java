package com.twt.service.bike.bike.ui.announcement;

import android.content.Context;

import com.twt.service.bike.common.BikePresenter;

/**
 * Created by jcy on 16-8-20.
 */
public class AnnouncementPresenter extends BikePresenter {
    private AnnouncementViewController mViewController;

    public AnnouncementPresenter(Context context, AnnouncementViewController viewController) {
        super(context);
        mViewController = viewController;
    }
}
