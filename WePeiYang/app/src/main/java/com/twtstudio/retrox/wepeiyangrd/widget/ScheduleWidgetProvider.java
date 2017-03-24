package com.twtstudio.retrox.wepeiyangrd.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.twtstudio.retrox.wepeiyangrd.R;
import com.twtstudio.retrox.wepeiyangrd.home.HomeActivity;

/**
 * Created by retrox on 24/03/2017.
 */

public class ScheduleWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_schedule);
            remoteViews.setOnClickPendingIntent(remoteViews.getLayoutId(),pendingIntent);

//            remoteViews.
            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);

        }
    }
}
