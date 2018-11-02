package com.twtstudio.tjliqy.party.bean;

import java.util.List;

/**
 * Created by tjliqy on 2016/8/18.
 */
public class CourseDetailInfo {

    private int status;

    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String article_id;
        private String course_id;
        private String article_name;
        private String article_content;
        private String article_ishidden;
        private String article_isdeleted;
        private String course_name;
        private String course_detail;
        private String course_priority;
        private String course_inserttime;
        private String course_ishidden;
        private String course_isdeleted;

        public String getArticle_id() {
            return article_id;
        }

        public void setArticle_id(String article_id) {
            this.article_id = article_id;
        }

        public String getCourse_id() {
            return course_id;
        }

        public void setCourse_id(String course_id) {
            this.course_id = course_id;
        }

        public String getArticle_name() {
            return article_name;
        }

        public void setArticle_name(String article_name) {
            this.article_name = article_name;
        }

        public String getArticle_content() {
            return article_content;
        }

        public void setArticle_content(String article_content) {
            this.article_content = article_content;
        }

        public String getArticle_ishidden() {
            return article_ishidden;
        }

        public void setArticle_ishidden(String article_ishidden) {
            this.article_ishidden = article_ishidden;
        }

        public String getArticle_isdeleted() {
            return article_isdeleted;
        }

        public void setArticle_isdeleted(String article_isdeleted) {
            this.article_isdeleted = article_isdeleted;
        }

        public String getCourse_name() {
            return course_name;
        }

        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }

        public String getCourse_detail() {
            return course_detail;
        }

        public void setCourse_detail(String course_detail) {
            this.course_detail = course_detail;
        }

        public String getCourse_priority() {
            return course_priority;
        }

        public void setCourse_priority(String course_priority) {
            this.course_priority = course_priority;
        }

        public String getCourse_inserttime() {
            return course_inserttime;
        }

        public void setCourse_inserttime(String course_inserttime) {
            this.course_inserttime = course_inserttime;
        }

        public String getCourse_ishidden() {
            return course_ishidden;
        }

        public void setCourse_ishidden(String course_ishidden) {
            this.course_ishidden = course_ishidden;
        }

        public String getCourse_isdeleted() {
            return course_isdeleted;
        }

        public void setCourse_isdeleted(String course_isdeleted) {
            this.course_isdeleted = course_isdeleted;
        }
    }
}
