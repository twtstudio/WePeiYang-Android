package com.twt.service.support;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.twt.service.R;


/**
 * Created by Rex on 2015/8/1.
 */
public class StatusBarHelper {
    private static com.readystatesoftware.systembartint.SystemBarTintManager tintManager;

    public static void setColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }
}
