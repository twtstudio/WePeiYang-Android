package com.twt.service.support;

import android.graphics.drawable.Drawable;

import com.twt.service.WePeiYangApp;

/**
 * Created by Rex on 2015/8/4.
 */
public class ResourceHelper {
    public static int getColor(int colorId) {
        return WePeiYangApp.getContext().getResources().getColor(colorId);
    }

    public static Drawable getDrawable(int drawableId) {
        return WePeiYangApp.getContext().getResources().getDrawable(drawableId);
    }

    public static String getString(int stringId) {
        return WePeiYangApp.getContext().getResources().getString(stringId);
    }
}
