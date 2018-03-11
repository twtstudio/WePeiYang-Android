package com.twt.wepeiyang.commons.utils;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by retrox on 2017/1/25.
 * 通过反射全局获取统一的app对象
 *
 * @deprecated use CommonContext instead
 */

@Deprecated
public class App {
    private static final WeakReference<Application> INSTANCE_REFERENCE;

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            Logger.e(e, "Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Logger.e(ex, "Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
            INSTANCE_REFERENCE = new WeakReference<>(app);
        }
    }

    public static Application getApplication() {
        return INSTANCE_REFERENCE.get();
    }

    public static Context getApplicationContext() {
        return INSTANCE_REFERENCE.get().getApplicationContext();
    }
}