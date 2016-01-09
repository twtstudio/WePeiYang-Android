package com.twt.service.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by sunjuntao on 15/12/5.
 */
public class Schedule extends DataSupport {

    @Column(defaultValue = "")
    String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
