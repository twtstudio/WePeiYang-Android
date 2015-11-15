package com.rex.wepeiyang.support;

import android.app.Activity;
import android.os.Build;


/**
 * Created by Rex on 2015/8/1.
 */
public class StatusBarHelper {
    public static void setStatusBar(Activity activity, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // create our manager instance after the content view is set
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            // enable status bar tint
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(color);
        }
    }
}
