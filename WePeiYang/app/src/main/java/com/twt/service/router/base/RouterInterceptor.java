package com.twt.service.router.base;

/**
 * Created by huangyong on 16/5/18.
 */
public interface RouterInterceptor {

    String getName();

    boolean intercept(String url);

}
