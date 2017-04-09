package com.twtstudio.retrox.news.api.bean;

import java.util.List;

/**
 * Created by retrox on 25/03/2017.
 */

public class CommonNewsBean {

    /**
     * error_code : -1
     * data : [{"index":"","subject":"标题","pic":"http://新闻封面图片","summary":"新闻摘要","visitcount":"阅读量","comments":"评论数"}]
     */

    public int error_code;
    public String message;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * index :
         * subject : 标题
         * pic : http://新闻封面图片
         * summary : 新闻摘要
         * visitcount : 阅读量
         * comments : 评论数
         */

        public String index;
        public String subject;
        public String pic;
        public String summary;
        public String visitcount;
        public String comments;
    }
}
