package com.twt.service.rxsrc.bike.ui.record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.rxsrc.common.ui.BaseAdapter;
import com.twt.service.rxsrc.common.ui.BaseViewHolder;
import com.twt.service.rxsrc.model.BikeRecord;
import com.twt.service.rxsrc.utils.TimeStampUtils;

import butterknife.InjectView;

/**
 * Created by jcy on 2016/9/10.
 */
public class RecordAdapter extends BaseAdapter<BikeRecord> {

    private static final int ITEM_NORMAL = 0;
    private static final int ITEM_FOOTER = 1;

    static class sRecordHolder extends BaseViewHolder{

        @InjectView(R.id.item_record_time_range)
        TextView mRangeText;
        @InjectView(R.id.item_record_time)
        TextView mTimeText;
        @InjectView(R.id.item_record_money)
        TextView mCostText;

        public sRecordHolder(View itemView) {
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
    public RecordAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == ITEM_FOOTER){
            return new FooterHolder(inflater.inflate(R.layout.footer,parent,false));
        }else {
            return new sRecordHolder(inflater.inflate(R.layout.item_bike_record,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ITEM_NORMAL){
            sRecordHolder itemHolder = (sRecordHolder) holder;
            final BikeRecord item = mDataSet.get(position);
            itemHolder.mRangeText.setText("骑行: "+getFormattedValue(Integer.parseInt(item.duration)));
            itemHolder.mTimeText.setText(TimeStampUtils.getDateString(item.arr_time));
            itemHolder.mCostText.setText("-"+item.fee);
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

    public String getFormattedValue(int intValue) {
        if (intValue == 0) {
            return 0 + "";
        }
        int more = intValue % 60;
        int min = (intValue - more) / 60;
        if (min > 0) {
            return min + "'" + more + "\"";
        } else {
            return more + "\"";
        }
    }
}
