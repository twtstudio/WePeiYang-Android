package com.rex.wepeiyang.api;

import com.rex.wepeiyang.JniUtils;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by sunjuntao on 15/11/6.
 */
public class RequestParams extends HashMap<String, String>{
    public RequestParams(){
        String timeStamp = Calendar.getInstance().getTimeInMillis()+"";
        JniUtils jniUtils = new JniUtils();
        this.put("app_key", jniUtils.getAppKey());
        this.put("t", timeStamp);
    }
}
