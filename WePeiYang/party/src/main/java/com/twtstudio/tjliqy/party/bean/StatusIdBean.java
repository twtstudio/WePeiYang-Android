package com.twtstudio.tjliqy.party.bean;

/**
 * Created by dell on 2016/7/22.
 */
public class StatusIdBean {
    private int id;
    private int status;//0 表示还没通过，1表示正在进行，2表示已经完成
    private String msg;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}