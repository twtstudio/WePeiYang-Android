package com.twt.service.widget;

import android.content.Context;
import android.content.Intent;

/**
 * Created by retrox on 27/03/2017.
 */

public class WidgetUpdateManger {
    public static void sendUpdateMsg(Context context) {
        Intent intent = new Intent("com.twt.appwidget.refresh");
        context.sendBroadcast(intent);
    }
}
