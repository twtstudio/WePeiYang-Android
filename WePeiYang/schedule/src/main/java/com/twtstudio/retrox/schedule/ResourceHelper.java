package com.twtstudio.retrox.schedule;

import android.graphics.drawable.Drawable;

import com.twt.wepeiyang.commons.utils.App;


/**
 * Created by Rex on 2015/8/4.
 */
public class ResourceHelper {
    public static int getColor(int colorId) {
        return App.getApplicationContext().getResources().getColor(colorId);
    }

    public static Drawable getDrawable(int drawableId) {
        return App.getApplicationContext().getResources().getDrawable(drawableId);
    }

    public static String getString(int stringId) {
        return App.getApplicationContext().getResources().getString(stringId);
    }
}
