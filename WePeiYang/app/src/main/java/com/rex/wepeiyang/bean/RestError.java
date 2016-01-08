package com.rex.wepeiyang.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunjuntao on 16/1/1.
 */
public class RestError {
    @SerializedName("error_code")
    public int error_code;
    @SerializedName("message")
    public String message;
}
