package com.twtstudio.retrox.classroomcore.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.orhanobut.hawk.Hawk;
import com.twtstudio.retrox.classroomcore.BR;
import com.twtstudio.retrox.classroomcore.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.tatarka.bindingcollectionadapter.ItemView;


/**
 * Created by retrox on 2017/2/22.
 */

public class ClassroomViewModel implements ViewModel{

    private Context context;

    public final ObservableField<String> message = new ObservableField<>();

    public final ObservableBoolean isProgressing = new ObservableBoolean(false);

    public final ObservableArrayList<BuildingItemViewModel> viewModels = new ObservableArrayList<>();

    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.item_common_classroom_building);

    public final ReplyCommand refreshClick = new ReplyCommand(()->{
        setQueryTime();
        for (BuildingItemViewModel viewModel : viewModels) {
            viewModel.getData();
        }
    });

    public final ReplyCommand editClick = new ReplyCommand(this::editPreference);

    public final ReplyCommand jumpToDetailClick = new ReplyCommand(this::jumpToDetail);

    private void setQueryTime(){
        SimpleDateFormat dateFormate = new SimpleDateFormat("HH:mm", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String s = dateFormate.format(date);
        message.set("查询时间: "+s);
    }

    public ClassroomViewModel(Context context) {
        this.context = context;
        queryClassrooms();
//        viewModels.add(new BuildingItemViewModel(45,context));
//        viewModels.add(new BuildingItemViewModel(46,context));
//        viewModels.add(new BuildingItemViewModel(55,context));
    }

    private void queryClassrooms(){
        setQueryTime();
        viewModels.clear();

        boolean[] defaultValues = new boolean[55];
        defaultValues[45] = true;
        defaultValues[43] = true;
        defaultValues[44] = true;

        boolean[] booleen = Hawk.get("pref_classroom_set",defaultValues);
        for (int i = 0; i < booleen.length; i++) {
            if (booleen[i]){
                viewModels.add(new BuildingItemViewModel(i+1,context));
            }
        }
    }

    private void editPreference(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        Hawk.delete("pref_classroom_set");
        boolean[] defaultValues = new boolean[55];
        defaultValues[45] = true;
        defaultValues[43] = true;
        defaultValues[44] = true;

        boolean[] booleen = Hawk.get("pref_classroom_set",defaultValues);
        String[] strings = new String[55];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = String.valueOf(i+1);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
//                .setIcon(R.drawable.classroom_icon)
                .setTitle("设置偏好教学楼(建议不超过4个)")
                .setMultiChoiceItems(strings, booleen, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        booleen[which] = isChecked;
                        Hawk.put("pref_classroom_set",booleen);
                        queryClassrooms();
                    }
                });

        builder.create().show();
    }

    private void jumpToDetail(){
        Class clazz = null ;
        try {
//            clazz = Class.forName("course.labs.classroomquery.homePage.MainActivity");
            clazz = Class.forName("com.twtstudio.service.classroom.view.MainActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(context,clazz);
        context.startActivity(intent);
    }
}
