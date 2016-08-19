package com.twt.service.bike.api;

import java.io.Serializable;

/**
 * Created by jcy on 2016/8/7.
 */

public class BikeApiResponse<T> implements Serializable {

    private int errno;
    private String errmsg;
    private T data;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
