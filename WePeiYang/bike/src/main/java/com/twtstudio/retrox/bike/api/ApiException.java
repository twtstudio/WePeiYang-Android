package com.twtstudio.retrox.bike.api;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcy on 2016/8/7.
 * @TwtStudio Mobile Develope Team
 */

/**
 * wpy通用API封装
 */
public class ApiException extends RuntimeException {
    protected static final List<Integer> AUTH_ERROR_CODES = new ArrayList<>();

    static {
        AUTH_ERROR_CODES.add(21);
        AUTH_ERROR_CODES.add(22);
        AUTH_ERROR_CODES.add(23);
    }

    private ApiResponse mResult;

    public ApiException(ApiResponse Result) {
        this.mResult = Result;
    }

    public int getCode() {
        return mResult.getError_code();
    }

    @Override
    public String getMessage() {
        return mResult.getMessage();
    }

    public boolean isAuthError() {
        return AUTH_ERROR_CODES.contains(getCode());
    }
}
