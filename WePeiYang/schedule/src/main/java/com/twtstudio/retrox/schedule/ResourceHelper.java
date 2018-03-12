package com.twtstudio.retrox.schedule;

import android.graphics.drawable.Drawable;

import com.twt.wepeiyang.commons.experimental.CommonContext;


/**
 * Created by Rex on 2015/8/4.
 */
public class ResourceHelper {
    public static int getColor(int colorId) {
        return CommonContext.INSTANCE.getApplication().getResources().getColor(colorId);
    }

    public static Drawable getDrawable(int drawableId) {
        return CommonContext.INSTANCE.getApplication().getResources().getDrawable(drawableId);
    }

    public static String getString(int stringId) {
        return CommonContext.INSTANCE.getApplication().getResources().getString(stringId);
    }
}
