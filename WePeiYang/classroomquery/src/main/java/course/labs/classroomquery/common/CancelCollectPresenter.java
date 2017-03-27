package course.labs.classroomquery.common;

import android.content.Context;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import course.labs.classroomquery.API.apiClient;
import course.labs.classroomquery.API.apisubscriber;
import course.labs.classroomquery.API.onErrorListener;
import course.labs.classroomquery.API.onNextListener;

/**
 * Created by Administrator on 2017/2/19.
 */

public class CancelCollectPresenter {
    private Context mContext;
    private CancelCollectController cotroller;
    public CancelCollectPresenter(Context context,CancelCollectController cotroller){
        this.cotroller = cotroller;
        this.mContext = context;
    }

    public void cancelCollect(String building){
        onErrorListener onErrorListener = new onErrorListener() {
            @Override
            public void OnError(Throwable e) {
                cotroller.onError();
            }
        };
        onNextListener<Integer> onNextListener = new onNextListener<Integer>() {
            @Override
            public void OnNext(Integer data) {
                cotroller.onNext(data.intValue());
            }
        };
        apisubscriber apisubscriber = new apisubscriber(mContext,onNextListener,onErrorListener);
        (new apiClient()).cancelCollect(this,apisubscriber,CommonPrefUtil.getToken(),building);
    }
}
