package com.twt.service.network.view.widget;

import android.content.Context;
import android.content.Intent;

/**
 * Created by chen on 2017/9/12.
 */

public class WidgetUpdateManager {
    public static void sendUpdateMsg(Context context) {
        Intent intent = new Intent("com.twt.appwidget");
        context.sendBroadcast(intent);
    }
}
