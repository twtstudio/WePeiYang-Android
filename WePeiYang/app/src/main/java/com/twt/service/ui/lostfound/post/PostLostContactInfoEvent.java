package com.twt.service.ui.lostfound.post;

/**
 * Created by sunjuntao on 16/3/12.
 */
public class PostLostContactInfoEvent {
    private String name;
    private String number;

    public PostLostContactInfoEvent(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
