package com.rex.wepeiyang.api;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by sunjuntao on 15/11/6.
 */
public class RequestParams extends HashMap<String, String>{
    public RequestParams(){
        String timeStamp = Calendar.getInstance().getTimeInMillis()+"";
        this.put("app_key", Constant.APPKEY);
        this.put("t", timeStamp);
    }
}
