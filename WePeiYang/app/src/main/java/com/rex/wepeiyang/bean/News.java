package com.rex.wepeiyang.bean;

/**
 * Created by sunjuntao on 15/11/12.
 */
public class News {

    public Data data;
    public int error_code;
    public int message;


    public class Data {
        public String content;
        public String gonggao;//供稿
        public int index;
        public String newscome;//新闻来源
        public String shenggao;//审稿
        public String sheying;//摄影
        public String subject;//题目
        public String visitcount;//阅读量
    }
}
