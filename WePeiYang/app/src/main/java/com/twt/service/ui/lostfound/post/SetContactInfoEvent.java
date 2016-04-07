package com.twt.service.ui.lostfound.post;

/**
 * Created by sunjuntao on 16/4/7.
 */
public class SetContactInfoEvent {
    private String name;
    private String phone;

    public SetContactInfoEvent(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
