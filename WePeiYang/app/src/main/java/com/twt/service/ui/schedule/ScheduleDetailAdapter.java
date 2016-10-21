package com.twt.service.ui.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.rxsrc.common.ui.BaseAdapter;
import com.twt.service.rxsrc.common.ui.BaseViewHolder;

import butterknife.InjectView;

/**
 * Created by jcy on 2016/9/24.
 */

public class ScheduleDetailAdapter extends BaseAdapter<ScheduleDetail.Content> {

    private static int DETAIL_INFO = 0;
    private static int DETAIL_CONTENT = 1;

    static class sDetailInfoHolder extends BaseViewHolder{
        @InjectView(R.id.schedule_detail_info)
        TextView mTextView;
        @InjectView(R.id.schedule_detail_divider)
        View mView;

        public sDetailInfoHolder(View itemView) {
            super(itemView);
        }
    }

    static class sDetailContentHolder extends BaseViewHolder{
        @InjectView(R.id.schedule_detail_content_key)
        TextView mContentKeyText;
        @InjectView(R.id.schedule_detail_content_value)
        TextView mContentValueText;

        public sDetailContentHolder(View itemView) {
            super(itemView);
        }
    }

    public ScheduleDetailAdapter(Context context,ScheduleDetail detail) {
        super(context);
        mDataSet = detail.mContentList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == DETAIL_INFO){
            return new sDetailInfoHolder(inflater.inflate(R.layout.item_schedule_detail_info,parent,false));
        }else if (viewType == DETAIL_CONTENT){
            return new sDetailContentHolder(inflater.inflate(R.layout.item_schedule_detail_content,parent,false));
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == DETAIL_INFO){
            sDetailInfoHolder itemHolder = (sDetailInfoHolder) holder;
            if (position == getItemCount()-1){
                itemHolder.mView.setVisibility(View.GONE);
            }
            final ScheduleDetail.Content item = mDataSet.get(position);
            itemHolder.mTextView.setText(item.getKey());
        }else if (type == DETAIL_CONTENT){
            sDetailContentHolder itemHolder = (sDetailContentHolder) holder;
            final ScheduleDetail.Content item = mDataSet.get(position);
            itemHolder.mContentKeyText.setText(item.getKey());
            itemHolder.mContentValueText.setText(item.getValue());
        }
    }

    @Override
    public int getItemViewType(int position) {
        ScheduleDetail.Content content = mDataSet.get(position);
        if (content.isContent()){
            return DETAIL_CONTENT;
        }else {
            return DETAIL_INFO;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
