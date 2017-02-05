package com.twtstudio.retrox.bike.bike.ui.announcement;

import android.content.Context;

import com.twtstudio.retrox.bike.api.BikeApiClient;
import com.twtstudio.retrox.bike.api.BikeApiSubscriber;
import com.twtstudio.retrox.bike.api.OnNextListener;
import com.twtstudio.retrox.bike.common.BikePresenter;
import com.twtstudio.retrox.bike.model.BikeAnnouncement;

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
