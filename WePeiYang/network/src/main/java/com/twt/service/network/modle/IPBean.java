package com.twt.service.network.modle;

/**
 * Created by chen on 2017/7/15.
 */

public class IPBean {
    public int error_code;
    public String message;
    public String data;
    public class data{
        public String ip;
    }

//    public String getData() {
//        if(error_code==-1){
//            return data;
//        }else{
//            throw new ApiException(error_code,message);
//        }
//    }
}
