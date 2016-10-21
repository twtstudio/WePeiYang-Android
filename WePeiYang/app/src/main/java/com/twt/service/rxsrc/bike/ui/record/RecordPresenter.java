package com.twt.service.rxsrc.bike.ui.record;

import android.content.Context;

import com.twt.service.rxsrc.api.BikeApiClient;
import com.twt.service.rxsrc.api.BikeApiSubscriber;
import com.twt.service.rxsrc.api.OnNextListener;
import com.twt.service.rxsrc.common.Presenter;
import com.twt.service.rxsrc.model.BikeRecord;
import com.twt.service.rxsrc.utils.TimeStampUtils;

import java.util.List;

/**
 * Created by jcy on 2016/9/9.
 */
public class RecordPresenter extends Presenter{
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
