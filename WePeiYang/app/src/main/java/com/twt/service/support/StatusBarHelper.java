package com.twt.service.support;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * Created by Rex on 2015/8/1.
 */
public class StatusBarHelper {
    private static com.readystatesoftware.systembartint.SystemBarTintManager tintManager;
    public static void setStatusBar(Activity activity, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintColor(color);
            tintManager.setStatusBarTintEnabled(true);
        }
    }
}
