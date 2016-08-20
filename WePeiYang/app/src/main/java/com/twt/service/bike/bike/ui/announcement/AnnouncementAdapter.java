package com.twt.service.bike.bike.ui.announcement;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.bike.common.ui.BaseAdapter;
import com.twt.service.bike.common.ui.BaseViewHolder;
import com.twt.service.bike.model.BikeAnnouncement;

/**
 * Created by jcy on 16-8-20.
 */
public class AnnouncementAdapter extends BaseAdapter<BikeAnnouncement>{

    private static final int ITEM_NORMAL = 0;
    private static final int ITEM_FOOTER = 1;

    static class sAnnouncementHolder extends BaseViewHolder{

        public sAnnouncementHolder(View itemView) {
            super(itemView);
        }
    }

    public AnnouncementAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }
}
