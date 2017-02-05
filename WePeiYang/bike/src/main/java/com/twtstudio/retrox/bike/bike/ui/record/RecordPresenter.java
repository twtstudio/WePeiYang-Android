package com.twtstudio.retrox.bike.bike.ui.record;

import android.content.Context;

import com.twtstudio.retrox.bike.api.BikeApiClient;
import com.twtstudio.retrox.bike.api.BikeApiSubscriber;
import com.twtstudio.retrox.bike.api.OnNextListener;
import com.twtstudio.retrox.bike.common.Presenter;
import com.twtstudio.retrox.bike.model.BikeRecord;
import com.twtstudio.retrox.bike.utils.TimeStampUtils;

import java.util.List;


/**
 * Created by jcy on 2016/9/9.
 */
public class RecordPresenter extends Presenter {
    private RecordViewController mController;

    public RecordPresenter(Context context,RecordViewController recordViewController) {
        super(context);
        mController = recordViewController;
    }

    public void getCurrentRecordList(){
        String time = TimeStampUtils.getSimpleMonthString(String.valueOf(System.currentTimeMillis()));
        BikeApiClient.getInstance().getBikeRecord(mContext,new BikeApiSubscriber(mContext,mOnNextListener),time);
    }

    private OnNextListener<List<BikeRecord>> mOnNextListener = new OnNextListener<List<BikeRecord>>() {
        @Override
        public void onNext(List<BikeRecord> bikeRecords) {
            mController.refreshItems(bikeRecords);
        }
    };
}
