package com.twtstudio.retrox.classroomcore.home;

import android.content.Context;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.classroomcore.home.api.ClassroomQueryProvider;

import rx.Observable;

/**
 * Created by retrox on 2017/2/24.
 */

public class BuildingItemViewModel implements ViewModel {

    private Context mContext;

    public final ObservableField<String> buildingName = new ObservableField<>();

    public final ObservableField<String> availableRooms = new ObservableField<>();

    private int buildingNum;

    public BuildingItemViewModel(int num, Context context) {
        mContext = context;
        this.buildingNum = num;
        getData();
    }

    public void getData() {
        ClassroomQueryProvider provider = new ClassroomQueryProvider(mContext);
        buildingName.set(buildingNum + "楼");
        availableRooms.set("查询中...");
        provider.getClassRoom(buildingNum, classroomQueryBean -> {
            availableRooms.set("");
            if (classroomQueryBean.errorcode == 1 || classroomQueryBean.data.size() == 0) {
                availableRooms.set("不支持查询此教学楼");
            } else {
                Observable.from(classroomQueryBean.data)
                        .filter(dataBean -> dataBean.state.equals("空闲")) //筛选掉上课中
                        .take(6)
                        .subscribe(dataBean -> {
                            String s = availableRooms.get();
                            //去掉后台返回的 xx楼前缀
                            s = s + dataBean.room.replace(buildingName.get(), "") + "  ";
                            availableRooms.set(s);
                        });
            }
        });
    }
}
