package com.twtstudio.retrox.news.detail;

import java.io.Serializable;

/**
 * Created by sunjuntao on 15/11/12.
 */
public class News implements Serializable{

    public Data data;
    public int error_code;
    public String message;


    public class Data implements Serializable{
        public String content;
        public String gonggao;//供稿
        public int index;
        public String newscome;//新闻来源
        public String shengao;//审稿
        public String sheying;//摄影
        public String subject;//题目
        public int visitcount;//阅读量
//        public List<Comment> comments;//评论
    }
}
