package com.twtstudio.retrox.bike.bike.ui.record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.R2;
import com.twtstudio.retrox.bike.common.ui.BaseAdapter;
import com.twtstudio.retrox.bike.common.ui.BaseViewHolder;
import com.twtstudio.retrox.bike.model.BikeRecord;
import com.twtstudio.retrox.bike.utils.TimeStampUtils;

import butterknife.BindView;


/**
 * Created by jcy on 2016/9/10.
 */
public class RecordAdapter extends BaseAdapter<BikeRecord> {

    private static final int ITEM_NORMAL = 0;
    private static final int ITEM_FOOTER = 1;

    static class sRecordHolder extends BaseViewHolder{

        @BindView(R2.id.item_record_time_range)
        TextView mRangeText;
        @BindView(R2.id.item_record_time)
        TextView mTimeText;
        @BindView(R2.id.item_record_money)
        TextView mCostText;

        public sRecordHolder(View itemView) {
            super(itemView);
        }
    }

    static class FooterHolder extends BaseViewHolder {

        @BindView(R2.id.pb_footer)
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
