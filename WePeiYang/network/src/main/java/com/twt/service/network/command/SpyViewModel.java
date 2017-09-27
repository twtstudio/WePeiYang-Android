package com.twt.service.network.command;

import android.content.Context;

import com.kelin.mvvmlight.base.ViewModel;

/**
 * Created by chen on 2017/7/11.
 */

public class SpyViewModel implements ViewModel {
    public Context mContext;

    public SpyViewModel(Context context) {
        this.mContext = context;
    }

    public String startTime;
    public String exactTime ;
    public String totalTime ;
    public String ipAddress;


}
