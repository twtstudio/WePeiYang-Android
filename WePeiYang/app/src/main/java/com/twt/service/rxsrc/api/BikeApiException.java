package com.twt.service.rxsrc.api;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcy on 2016/8/7.
 */

public class BikeApiException extends RuntimeException {
    protected static final List<Integer> AUTH_ERROR_CODES = new ArrayList<>();

    static {
        AUTH_ERROR_CODES.add(21);
        AUTH_ERROR_CODES.add(22);
        AUTH_ERROR_CODES.add(23);
    }

    private BikeApiResponse mResult;

    public BikeApiException(BikeApiResponse Result) {
        this.mResult = Result;
    }

    public int getCode() {
        return mResult.getErrno();
    }

    @Override
    public String getMessage() {
        return mResult.getErrmsg();
    }

    public boolean isAuthError() {
        return AUTH_ERROR_CODES.contains(getCode());
    }
}
