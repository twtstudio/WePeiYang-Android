package com.twt.service.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by huangyong on 16/5/18.
 */
public class APIResponse<T> implements Serializable {

    @SerializedName("error_code")
    private int errorCode;

    private String message;

    private T data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int code) {
        this.errorCode = code;
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

