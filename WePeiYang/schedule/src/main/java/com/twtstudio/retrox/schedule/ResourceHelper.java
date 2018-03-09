package com.twtstudio.retrox.schedule;

import android.graphics.drawable.Drawable;

import com.twt.wepeiyang.commons.experimental.Commons;


/**
 * Created by Rex on 2015/8/4.
 */
public class ResourceHelper {
    public static int getColor(int colorId) {
        return Commons.INSTANCE.getApplicationContext().getResources().getColor(colorId);
    }

    public static Drawable getDrawable(int drawableId) {
        return Commons.INSTANCE.getApplicationContext().getResources().getDrawable(drawableId);
    }

    public static String getString(int stringId) {
        return Commons.INSTANCE.getApplicationContext().getResources().getString(stringId);
    }
}
