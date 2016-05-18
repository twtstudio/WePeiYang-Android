package com.twt.service.router;

import android.content.Context;

import cn.campusapp.router.route.IRoute;
import cn.campusapp.router.router.BaseRouter;

/**
 * Created by huangyong on 16/5/18.
 */
public class ActionRouter extends BaseRouter {
    @Override
    public boolean open(IRoute iRoute) {
        return false;
    }

    @Override
    public boolean open(String s) {
        return false;
    }

    @Override
    public boolean open(Context context, String s) {
        return false;
    }

    @Override
    public IRoute getRoute(String s) {
        return null;
    }

    @Override
    public boolean canOpenTheRoute(IRoute iRoute) {
        return false;
    }

    @Override
    public boolean canOpenTheUrl(String s) {
        return false;
    }

    @Override
    public Class<? extends IRoute> getCanOpenRoute() {
        return ActionRoute.class;
    }
}
