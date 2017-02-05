package com.twtstudio.retrox.bike.api;

import java.io.Serializable;

/**
 * Created by jcy on 2016/8/7.
 */

public class ApiResponse<T> implements Serializable {

    private int error_code;
    private String message;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
