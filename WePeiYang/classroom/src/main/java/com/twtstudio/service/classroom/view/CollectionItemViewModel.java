package com.twtstudio.service.classroom.view;

import android.databinding.ObservableField;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.database.RoomCollection;
import com.twtstudio.service.classroom.utils.StringHelper;
import com.twtstudio.service.classroom.model.ClassRoomProvider;
import com.twtstudio.service.classroom.model.FreeRoom2;
import com.twtstudio.service.classroom.utils.TimeHelper;

import es.dmoral.toasty.Toasty;

/**
 * Created by DefaultAccount on 2017/8/21.
 */

public class CollectionItemViewModel implements com.kelin.mvvmlight.base.ViewModel {
    public final ObservableField<String> classroom = new ObservableField<>();
    public final ObservableField<String> campus = new ObservableField<>();
    public final ObservableField<String> time = new ObservableField<>();
    public final ObservableField<Integer> resid1 = new ObservableField<>();
    public final ObservableField<Integer> resid2 = new ObservableField<>();
    public final ObservableField<Integer> resid3 = new ObservableField<>();
    public final ObservableField<Integer> resid4 = new ObservableField<>();
    public final ObservableField<Boolean> isLoading=new ObservableField<>(false);
    public final ObservableField<Boolean> isTextVisible = new ObservableField<>(true); //是否显示tv_showstatus
    public final ObservableField<Boolean> isVisible5 = new ObservableField<>(false);
    public final ObservableField<Boolean> isVisible2 = new ObservableField<>(false);
    public final ObservableField<Boolean> isVisible3 = new ObservableField<>(false);
    public final ObservableField<Boolean> isVisible4 = new ObservableField<>(false);
    public final ObservableField<Boolean> isCollected = new ObservableField<>(false);
    public Integer freeRoomTime = 0;
    FreeRoom2.FreeRoom freeRoom;
    RxAppCompatActivity rxAppCompatActivity;
    Animation zoomIn, zoomOut;
    RoomCollection roomCollection;
    public ReplyCommand tvClickCommand = new ReplyCommand(() -> {
        isTextVisible.set(false);
        isLoading.set(true);
        ClassRoomProvider.init(rxAppCompatActivity).registerAction((freeRoom2) -> {
            isLoading.set(false);
            for (FreeRoom2.FreeRoom freeRoom : freeRoom2.getData()) {
                if (freeRoom.getRoom().equals(this.freeRoom.getRoom()))
                    if (freeRoom.getState().equals("空闲"))
                        resid1.set(R.drawable.classroom_tag_empty);
                    else if (freeRoom.getState().equals("上课中"))
                        resid1.set(R.drawable.classroom_tag_inclass);
            }
        }).getFreeClassroom(StringHelper.getBuildingInt(freeRoom.getRoom()), TimeHelper.getWeekInt(), TimeHelper.getDayOfWeek(), TimeHelper.getTimeInt(), CommonPrefUtil.getStudentNumber());
    });

    CollectionItemViewModel(RxAppCompatActivity rxAppCompatActivity, FreeRoom2.FreeRoom freeRoom) {
        zoomIn = AnimationUtils.loadAnimation(rxAppCompatActivity, R.anim.classroom_zoom_in);
        zoomOut = AnimationUtils.loadAnimation(rxAppCompatActivity, R.anim.classroom_zoom_out);
        this.freeRoom = freeRoom;
        this.rxAppCompatActivity = rxAppCompatActivity;
        if (StringHelper.getBuildingInt(freeRoom.getRoom())>30)
            campus.set("北洋园校区");
        else
            campus.set("卫津路校区");
        initData(freeRoom);
    }

    private void initData(FreeRoom2.FreeRoom freeRoom) {
        this.freeRoom = freeRoom;
        if (freeRoom.getHeating()) isVisible4.set(true);
        if (freeRoom.getWater_dispenser()) isVisible5.set(true);
        if (freeRoom.getPower_pack()) isVisible3.set(true);
        classroom.set(freeRoom.getRoom());
        isCollected.set(freeRoom.isCollected());
        roomCollection = freeRoom.toRoomCollection();
    }

    public void onClick(View v) {
        isCollected.set(!isCollected.get());

        if (isCollected.get() && roomCollection != null) {
            v.startAnimation(zoomIn);
            roomCollection.setCollection(true);
            ClassRoomProvider.init(rxAppCompatActivity).addCollectedClassRoom(roomCollection);
//            viewModel.setCollected(freeRoom.getRoom());
        } else {
            v.startAnimation(zoomOut);
            ClassRoomProvider.init(rxAppCompatActivity).deleteCollectedClassRoom(roomCollection);
//            viewModel.cancelCollected(freeRoom.getRoom());
            Toasty.normal(rxAppCompatActivity, "已取消收藏", Toast.LENGTH_SHORT).show();
        }
    }

}
