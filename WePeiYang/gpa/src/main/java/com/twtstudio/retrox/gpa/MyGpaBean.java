package com.twtstudio.retrox.gpa;

import com.twt.wepeiyang.commons.network.ApiException;

/**
 * Created by retrox on 2017/2/3.
 */

public class MyGpaBean {
    private int error_code;

    private String message;

    private GpaBean data;

    public int getError_code() {
        return error_code;
    }

    public String getMessage() {
        return message;
    }

    public GpaBean getData() {
        if (error_code == -1){
            return data;
        }else {
            throw new ApiException(error_code,message);
        }
    }
}
