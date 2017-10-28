package com.twt.service.network.command;

import android.content.Context;

/**
 * Created by chen on 2017/7/11.
 */

public class SpyCommand {
    public Context mContext;

    public SpyCommand(Context context) {
        this.mContext = context;
    }

    public String startTime;
    public String exactTime ;
    public String totalTime ;
    public String ipAddress;


}
