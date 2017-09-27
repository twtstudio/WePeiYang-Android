package com.twt.service.network.modle;

import com.twt.wepeiyang.commons.network.ApiException;

/**
 * Created by chen on 2017/7/14.
 */

public class StatusBean {
    public int error_code;
    public String message;
    public String data;
    public class data{
        public String ip;
        public String online;
    }

    public String getData() {
        if(error_code==-1){
            return data;
        }else{
            throw new ApiException(error_code,message);
        }
    }
}
