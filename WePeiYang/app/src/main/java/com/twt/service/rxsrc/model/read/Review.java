package com.twt.service.rxsrc.model.read;

import java.util.List;

/**
 * Created by jcy on 16-10-21.
 */

public class Review {

    public String content;
    public int rate;
    public int like;
    public String timestamp;

    public static class user {
        public String id;
        public String name;
        public String avatar;
    }

    public static class book {
        public String title;
        public String isbn;
    }
}
