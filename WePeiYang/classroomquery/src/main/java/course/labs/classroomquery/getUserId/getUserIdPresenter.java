package course.labs.classroomquery.getUserId;

import android.content.Context;
import android.util.Log;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import java.util.List;

import course.labs.classroomquery.API.apiClient;
import course.labs.classroomquery.API.apisubscriber;
import course.labs.classroomquery.API.onErrorListener;
import course.labs.classroomquery.API.onNextListener;
import course.labs.classroomquery.Model.ClassroomBean;
import course.labs.classroomquery.Model.FreeRoom2;
import course.labs.classroomquery.Model.userId;

/**
 * Created by Administrator on 2017/2/26.
 */

public class getUserIdPresenter {
    private getUserIdController mController;
    private Context mContext;
    public getUserIdPresenter(Context context,getUserIdController controller){
        mContext = context;
        mController  =controller;
    }

    public void getUserId(String token){
        onErrorListener onErrorListener = new onErrorListener() {
            @Override
            public void OnError(Throwable e) {
                mController.onError();
            }
        };
        onNextListener<userId> onNextListener = new onNextListener<userId>() {
            @Override
            public void OnNext(userId id) {
                mController.onNext(id);
            }
        };
        apisubscriber subscriber = new apisubscriber(mContext,onNextListener,onErrorListener);
        (new getUserIdClient()).getUserId(this,subscriber,token);
    }
}
