package com.twtstudio.retrox.wepeiyangrd.home.common.schedule;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.kelin.mvvmlight.base.ViewModel;

/**
 * Created by retrox on 2017/2/7.
 */

public class CourseDetailViewModel implements ViewModel {

    public final ObservableField<String> name = new ObservableField<>();

    public final ObservableField<String> time = new ObservableField<>();

    public final ObservableField<String> location = new ObservableField<>();

    public final ObservableInt color = new ObservableInt();
}
