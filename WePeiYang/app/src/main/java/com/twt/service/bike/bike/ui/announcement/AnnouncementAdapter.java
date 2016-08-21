package com.twt.service.bike.bike.ui.announcement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.bike.common.ui.BaseAdapter;
import com.twt.service.bike.common.ui.BaseViewHolder;
import com.twt.service.bike.model.BikeAnnouncement;
import com.twt.service.bike.utils.TimeStampUtils;

import butterknife.InjectView;

/**
 * Created by jcy on 16-8-20.
 */
public class AnnouncementAdapter extends BaseAdapter<BikeAnnouncement>{

    private static final int ITEM_NORMAL = 0;
    private static final int ITEM_FOOTER = 1;

    static class sAnnouncementHolder extends BaseViewHolder{
        @InjectView(R.id.bike_announcement_title)
        TextView mTitleView;
        @InjectView(R.id.bike_announcement_content)
        TextView mContentView;
        @InjectView(R.id.bike_announcement_time)
        TextView mTimeView;

        public sAnnouncementHolder(View itemView) {
            super(itemView);
        }
    }

    static class FooterHolder extends BaseViewHolder{

        @InjectView(R.id.pb_footer)
        ProgressBar mFooter;
        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    public AnnouncementAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == ITEM_FOOTER){
            return new FooterHolder(inflater.inflate(R.layout.footer,parent,false));
        }else {
            return new sAnnouncementHolder(inflater.inflate(R.layout.item_bike_announcement,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ITEM_NORMAL){
            sAnnouncementHolder itemHolder = (sAnnouncementHolder) holder;
            final BikeAnnouncement item = mDataSet.get(position);
            itemHolder.mTitleView.setText(item.title);
            itemHolder.mContentView.setText(item.content);
            itemHolder.mTimeView.setText(TimeStampUtils.getDateString(item.timestamp));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDataSet.size()) {
            return ITEM_FOOTER;
        } else {
            return ITEM_NORMAL;
        }
    }
}
