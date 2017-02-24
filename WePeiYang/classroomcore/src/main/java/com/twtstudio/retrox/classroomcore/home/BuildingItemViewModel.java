package com.twtstudio.retrox.classroomcore.home;

import android.content.Context;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.classroomcore.home.api.ClassroomQueryProvider;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by retrox on 2017/2/24.
 */

public class BuildingItemViewModel implements ViewModel {

    private Context mContext;

    public final ObservableField<String> buildingName = new ObservableField<>();

    public final ObservableField<String> availableRooms = new ObservableField<>("");

    private int buildingNum;

    public BuildingItemViewModel(int num,Context context) {
        mContext = context;
        this.buildingNum = num;
        getData();
    }

    private void getData() {
        ClassroomQueryProvider provider = new ClassroomQueryProvider(mContext);

        provider.getClassRoom(buildingNum, classroomQueryBean -> {
            buildingName.set(buildingNum+"楼");
            Observable.from(classroomQueryBean.data)
                    .take(6)
                    .subscribe(dataBean -> {
                        String s = availableRooms.get();
                        //去掉后台返回的 xx楼前缀
                        s = s + dataBean.room.replace(buildingName.get(),"")+"  ";
                        availableRooms.set(s);
                    });
        });
    }
}
