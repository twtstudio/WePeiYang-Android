package com.twt.service.bean;

/**
 * Created by sunjuntao on 16/2/19.
 */
public class LostDetails {
    public int error_code;
    public String message;
    public Data data;

    public class Data {
        public int id;
        public int type;
        public String name;
        public String title;
        public String place;
        public String time;
        public String phone;
        public String content;
        public int lost_type;
        public String other_tag;
    }
}
