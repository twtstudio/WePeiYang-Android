package com.twt.service.rxsrc.model.read;

import java.util.List;

/**
 * Created by jcy on 16-10-21.
 */

public class Detail {
    public String isbn;
    public String title;
    public String cover;
    public String author;
    public String publisher;
    public String year;
    public String summary;
    public String index;
    public int rate;

    public List<statusItem> status;

    public star_review starreview;

    public List<reviewItem> reviews;


    public static class star_review{
        public String username;
        public String avatar;
        public String content;
    }

    public static class statusItem{
        public String barcode;
        public String status;
        public String duetime;
        public String library;
        public String location;

    }

    public static class reviewItem{
        public String content;
        public int rate;
        public int like;
        public String timestamp;

        public static class user{
            public int id;
            public String name;
            public String avatar;
        }
    }
}
