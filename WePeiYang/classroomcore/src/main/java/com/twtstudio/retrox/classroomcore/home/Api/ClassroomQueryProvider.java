package com.twtstudio.retrox.classroomcore.home.api;

import android.content.Context;
import android.widget.Toast;

import com.twt.wepeiyang.commons.network.ApiException;
import com.twt.wepeiyang.commons.network.DefaultRetrofitBuilder;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/2/23.
 */

public class ClassroomQueryProvider {

    private ClassroomApi api;

    private Context mContext;

    public ClassroomQueryProvider(Context mContext) {
        this.mContext = mContext;
        api = DefaultRetrofitBuilder.getBuilder()
                .baseUrl("http://120.27.115.59/test_laravel/public/index.php/api/")
                .build().create(ClassroomApi.class);
    }

    public void getClassRoom(int buildingNum, Action1<ClassroomQueryBean> action1) {
        int week = getWeekInt(CommonPrefUtil.getStartUnix());
        api.getClassroomFromBuliding(buildingNum,week)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1,throwable -> {
                    if (throwable instanceof ApiException){
                        Toast.makeText(mContext,"网络错误",Toast.LENGTH_SHORT).show();
                    }else {
                        throwable.printStackTrace();
                    }
                });

    }

    private int getWeekInt(long startUnix) {
        long i = System.currentTimeMillis() / 1000 - startUnix;
        int day = (int) (i / 86400);
        int week = day / 7 + 1;
        //有人过了那个周数就想看下学期hhh

        return week;

    }

}
