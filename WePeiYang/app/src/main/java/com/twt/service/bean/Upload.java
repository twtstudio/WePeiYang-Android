package com.twt.service.bean;

import java.util.List;

/**
 * Created by sunjuntao on 16/2/26.
 */
public class Upload {
    public int error_code;
    public List<Data> data;

    public class Data {
        public String name;
        public String url;
    }
}
