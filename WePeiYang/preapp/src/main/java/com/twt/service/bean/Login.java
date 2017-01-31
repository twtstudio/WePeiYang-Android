package com.twt.service.bean;

/**
 * Created by sunjuntao on 16/1/1.
 */
public class Login {
    public int error_code;
    public String message;
    public Data data;
    public class Data{
        public String token;
    }
}
