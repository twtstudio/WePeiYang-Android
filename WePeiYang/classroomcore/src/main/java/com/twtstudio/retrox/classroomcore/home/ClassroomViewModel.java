package com.twtstudio.retrox.classroomcore.home;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;

/**
 * Created by retrox on 2017/2/22.
 */

public class ClassroomViewModel implements ViewModel{

    public final ObservableField<String> message = new ObservableField<>("查询完成 18:10");

    public final ObservableBoolean isProgressing = new ObservableBoolean(false);

}
