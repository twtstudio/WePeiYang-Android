package com.twt.service.bean;

import java.util.List;

/**
 * Created by jcy on 2016/7/17.
 */

public class LibSearch {

    /**
     * error_code : -1
     * message :
     * data:.....很多
     */

    private int error_code;
    private String message;
    /**
     * index : 754006
     * title : Swift程序设计实战入门
     * author : 蔡明志著
     * publisher : 机械工业出版社
     * location : TP312SW/C5
     */

    private List<DataBean> data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String index;
        private String title;
        private String author;
        private String publisher;
        private String location;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
