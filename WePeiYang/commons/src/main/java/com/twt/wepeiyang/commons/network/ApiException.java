package com.twt.wepeiyang.commons.network;

/**
 * Created by retrox on 2017/1/20.
 */

public class ApiException extends RuntimeException {
    public int error_code;
    public String message;

    public ApiException(int error_code, String message) {
        this.error_code = error_code;
        this.message = message;
    }

    public int getError_code() {
        return error_code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
