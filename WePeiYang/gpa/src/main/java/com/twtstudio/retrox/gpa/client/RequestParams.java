package com.twtstudio.retrox.gpa.client;


import com.twt.wepeiyang.commons.JniUtils;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by sunjuntao on 15/11/6.
 */
public class RequestParams extends HashMap<String, String>{
    public RequestParams(){
        String timeStamp = Calendar.getInstance().getTimeInMillis()+"";
        this.put("app_key", JniUtils.getInstance().getAppKey());
        this.put("t", timeStamp);
    }
}
