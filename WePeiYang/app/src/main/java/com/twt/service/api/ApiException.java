package com.twt.service.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyong on 16/5/18.
 */
public class ApiException extends RuntimeException {

    protected static final List<Integer> AUTH_ERROR_CODES = new ArrayList<>();

    static {
        AUTH_ERROR_CODES.add(10000);
        AUTH_ERROR_CODES.add(10001);
        AUTH_ERROR_CODES.add(10002);
    }

    private ApiResponse mResult;

    public ApiException(ApiResponse result) {
        mResult = result;
    }

    public int getCode() {
        return mResult.getErrorCode();
    }

    @Override
    public String getMessage() {
        return mResult.getMessage();
    }

    public boolean isAuthError() {
        return AUTH_ERROR_CODES.contains(getCode());
    }

}
