package com.twt.service.bean;

/**
 * Created by sunjuntao on 16/2/13.
 */
public class Jobs {
    public int error_code;
    public String message;
    public Data data;
    public class Data {
        public int id;
        public String title;
        public String content;
        public String corporation;
        public String deadline;
        public int click;
        public String date;
    }
}
