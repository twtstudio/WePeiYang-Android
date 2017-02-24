package com.twtstudio.retrox.bike.homeitem;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;

/**
 * Created by retrox on 2017/2/24.
 */

public class BikeHomeItemViewModel implements ViewModel{

    public final ObservableField<String> moneyLeft = new ObservableField<>("2.40元");
}
