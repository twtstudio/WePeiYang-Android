package com.twt.service.bean;

/**
 * Created by sunjuntao on 16/2/14.
 */
public class CommentCallback {
    public int error_code;
    public Data data;

    public class Data {
        public String cuser;
        public String ccontent;
        public String nid;
        public String cip;
        public String ctime;
    }
}
