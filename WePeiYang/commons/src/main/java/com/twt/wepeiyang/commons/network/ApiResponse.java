package com.twt.wepeiyang.commons.network;

import java.io.Serializable;

/**
 * Created by retrox on 2016/11/25.
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
        if (error_code == -1){
            return data;
        }else {
            throw new ApiException(error_code,message);
        }

    }

    public void setData(T data) {
        this.data = data;
    }
}
