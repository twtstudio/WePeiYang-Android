package com.twtstudio.retrox.bike.model.read;

/**
 * Created by jcy on 16-10-21.
 * @TwtStudio Mobile Develope Team
 */

public class MyReview {

    public String content;
    public int rate;
    public int like;
    public String timestamp;
    public Book book;


    public static class Book {
        public String title;
        public String isbn;
    }
}
