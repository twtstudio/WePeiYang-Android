package com.twt.service.rxsrc.bike.ui.announcement;

import android.content.Context;

import com.twt.service.rxsrc.api.BikeApiClient;
import com.twt.service.rxsrc.api.BikeApiSubscriber;
import com.twt.service.rxsrc.api.OnNextListener;
import com.twt.service.rxsrc.common.BikePresenter;
import com.twt.service.rxsrc.model.BikeAnnouncement;

import java.util.List;

/**
 * Created by jcy on 16-8-20.
 */
public class AnnouncementPresenter extends BikePresenter {
    private AnnouncementViewController mViewController;

    public AnnouncementPresenter(Context context, AnnouncementViewController viewController) {
        super(context);
        mViewController = viewController;
    }

    public void getAnnouncement() {
        BikeApiClient.getInstance().getAnnouncement(mContext, new BikeApiSubscriber<>(mContext, mListener));
    }

    private OnNextListener<List<BikeAnnouncement>> mListener = new OnNextListener<List<BikeAnnouncement>>() {
        @Override
        public void onNext(List<BikeAnnouncement> bikeAnnouncements) {
            mViewController.setAnnouncementList(bikeAnnouncements);
        }
    };
}
