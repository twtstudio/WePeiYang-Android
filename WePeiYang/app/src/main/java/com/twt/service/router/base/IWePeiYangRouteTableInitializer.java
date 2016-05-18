package com.twt.service.router.base;

import java.util.Map;

import cn.campusapp.router.router.IActivityRouteTableInitializer;

/**
 * Created by huangyong on 16/5/18.
 */
public interface IWePeiYangRouteTableInitializer extends IActivityRouteTableInitializer {

    void initInterceptorTable(Map<String, String> interceptorTable);

}
