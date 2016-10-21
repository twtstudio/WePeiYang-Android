package com.twt.service.rxsrc.api;

import java.io.Serializable;

/**
 * Created by jcy on 2016/8/7.
 */

public class ApiResponse<T> implements Serializable {

    private int err_code;
    private String message;
    private T data;

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
