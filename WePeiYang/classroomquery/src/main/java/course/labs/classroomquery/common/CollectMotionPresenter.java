package course.labs.classroomquery.common;

import android.content.Context;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import course.labs.classroomquery.API.APIReaponse;
import course.labs.classroomquery.API.apiClient;
import course.labs.classroomquery.API.apisubscriber;
import course.labs.classroomquery.API.onErrorListener;
import course.labs.classroomquery.API.onNextListener;

/**
 * Created by Administrator on 2017/2/9.
 */

public class CollectMotionPresenter {
    private Context mContext;
    private CollectMotionCotroller cotroller;
    public CollectMotionPresenter(Context context,CollectMotionCotroller cotroller){
        this.cotroller = cotroller;
        this.mContext = context;
    }
    public void collect(String building){
        onErrorListener onErrorListener = new onErrorListener() {
            @Override
            public void OnError(Throwable e) {
                cotroller.onError();
            }
        };
        onNextListener onNextListener = new onNextListener() {
            @Override
            public void OnNext(Object o) {
                cotroller.onNext();
            }
        };
        apisubscriber subscriber = new apisubscriber(mContext,onNextListener,onErrorListener);
        (new apiClient()).collect(this,subscriber,building, CommonPrefUtil.getToken());
    }
    public void cancelCollect(String building){
        onErrorListener onErrorListener = new onErrorListener() {
            @Override
            public void OnError(Throwable e) {
                cotroller.onError();
            }
        };
        onNextListener onNextListener = new onNextListener() {
            @Override
            public void OnNext(Object o) {
                cotroller.onNext();
            }
        };
        apisubscriber apisubscriber = new apisubscriber(mContext,onNextListener,onErrorListener);
        (new apiClient()).cancelCollect(this,apisubscriber,CommonPrefUtil.getToken(),building);
    }
}
