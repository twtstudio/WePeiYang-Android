package com.twtstudio.service.classroom.view;

import android.databinding.BindingConversion;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.database.RoomCollection;
import com.twtstudio.service.classroom.model.ClassRoomProvider;
import com.twtstudio.service.classroom.model.FreeRoom2;
import com.twtstudio.service.classroom.utils.TimeHelper;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import com.twtstudio.service.classroom.utils.PrefUtil;
import static com.twt.wepeiyang.commons.utils.App.getApplicationContext;

/**
 * Created by zhangyulong on 7/11/17.
 */

public class ItemViewModel implements com.kelin.mvvmlight.base.ViewModel {
    public final ObservableField<String> classroom = new ObservableField<>();
    public final ObservableField<String> campus = new ObservableField<>();
    public final ObservableField<String> time = new ObservableField<>();
    public final ObservableField<Integer> resid1 = new ObservableField<>();
    public final ObservableField<Integer> resid2 = new ObservableField<>();
    public final ObservableField<Integer> resid3 = new ObservableField<>();
    public final ObservableField<Integer> resid4 = new ObservableField<>();
    public final ObservableField<Boolean> isVisible5 = new ObservableField<>(false);
    public final ObservableField<Boolean> isVisible2 = new ObservableField<>(false);
    public final ObservableField<Boolean> isVisible3 = new ObservableField<>(false);
    public final ObservableField<Boolean> isVisible4 = new ObservableField<>(false);
    public final ObservableField<Boolean> isCollected = new ObservableField<>(false);
    public Integer freeRoomTime = 0;
    FreeRoom2.FreeRoom freeRoom;
    RxAppCompatActivity rxAppCompatActivity;
    MainActivityViewModel viewModel;
    Animation zoomIn, zoomOut;
    RoomCollection roomCollection;

    ItemViewModel(RxAppCompatActivity rxAppCompatActivity, FreeRoom2.FreeRoom freeRoom, MainActivityViewModel viewModel, int freeRoomTime) {
        zoomIn = AnimationUtils.loadAnimation(rxAppCompatActivity, R.anim.classroom_zoom_in);
        zoomOut = AnimationUtils.loadAnimation(rxAppCompatActivity, R.anim.classroom_zoom_out);
        this.rxAppCompatActivity = rxAppCompatActivity;
        this.viewModel = viewModel;
        this.freeRoomTime = freeRoomTime;
        if (viewModel.condition2.get().equals("现在") && freeRoomTime == TimeHelper.getTimeInt()){
            int hours = Calendar.getInstance().getTime().getHours();
            int minutes = Calendar.getInstance().getTime().getMinutes();
            time.set(hours + ":" + (minutes < 10 ? "0" + minutes : minutes));
        } else if (freeRoomTime % 2 == 1)
            time.set("第" + freeRoomTime + "节-" + "第" + (freeRoomTime + 1) + "节");
        else
            time.set("第" + (freeRoomTime - 1) + "节-" + "第" + freeRoomTime + "节");
//        String test=df.format(Calendar.getInstance()).toString();
        if (PrefUtil.getIsNewCampus())
            campus.set("北洋园校区");
        else
            campus.set("卫津路校区");
        initData(freeRoom);
    }

    private void initData(FreeRoom2.FreeRoom freeRoom) {
//        Uri uri1=Uri.parse("android.resource://" + rxAppCompatActivity.getApplicationContext().getPackageName() + "/" +R.drawable.classroom_tag_empty);
        if (freeRoom.getState().equals("空闲"))
            resid1.set(R.drawable.classroom_tag_empty);
        else if (freeRoom.getState().equals("上课中"))
            resid1.set(R.drawable.classroom_tag_inclass);
        if (freeRoom.getState().equals("即将上课"))
            resid1.set(R.drawable.classroom_tag_willhaveclass);
        if (freeRoom.getState().equals("即将下课"))
            resid1.set(R.drawable.classroom_tag_willbeempty);
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

    @BindingConversion
    public static Drawable convertResIdToDrawable(int resId) {
        return ContextCompat.getDrawable(getApplicationContext(), resId);
    }

}
