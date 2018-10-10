package com.twt.service.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.LiveData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.twt.service.R;
import com.twt.service.home.HomeNewActivity;
import com.twt.service.schedule2.model.Course;
import com.twt.service.schedule2.model.MergedClassTableProvider;
import com.twt.service.schedule2.model.total.TotalCourseManager;
import com.twt.service.schedule2.view.schedule.ScheduleActivity;
import com.twtstudio.retrox.schedule.TimeHelper;
import com.twtstudio.retrox.schedule.model.CourseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import rx.Observable;

/**
 * Created by retrox on 24/03/2017.
 */

public class ScheduleWidgetProvider extends AppWidgetProvider {

    public static final SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
    private AppWidgetManager manager = null;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "com.twt.appwidget.refresh")) {
            ComponentName name = new ComponentName(context, ScheduleWidgetProvider.class);
            ScheduleWidgetProvider.this.onUpdate(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context).getAppWidgetIds(name));
            Logger.d("widget refresh click!");
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        CourseHelper.setCalendar(CalendarDay.today());
        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, HomeNewActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_schedule);
            remoteViews.setOnClickPendingIntent(R.id.widget_framelayout, pendingIntent);

            Intent imageClickIntent = new Intent(context, ScheduleWidgetProvider.class);
            imageClickIntent.setAction("com.twt.appwidget.refresh");
            PendingIntent imageClickPendingIntent = PendingIntent.getBroadcast(context, 0, imageClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_image_button, imageClickPendingIntent);

            remoteViews.setTextViewText(R.id.widget_today_date, getTodayString());

            manager = appWidgetManager;
            getData(context, appWidgetId, remoteViews, false);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        }
    }

    private void setupList(Context context, int appWidgetId, RemoteViews remoteViews, ArrayList list) {
        Intent serviceIntent = new Intent(context, WidgetService.class);

        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        Hawk.put("scheduleCache2", list);

        Intent startActivityIntent = new Intent(context, ScheduleActivity.class);
        PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_listview, startActivityPendingIntent);

        remoteViews.setRemoteAdapter(R.id.widget_listview, serviceIntent);
        remoteViews.setEmptyView(R.id.widget_listview, R.id.widget_empty_list);

        manager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview);
        manager.updateAppWidget(appWidgetId, remoteViews);

    }

    public void getData(Context context, int appWidgetId, RemoteViews remoteViews, boolean update) {

        LiveData<MergedClassTableProvider> classTableProviderLiveData =  TotalCourseManager.INSTANCE.getTotalCourseManager(false, false, false, cacheIndicatorRefreshState -> null);
        classTableProviderLiveData.observeForever(mergedClassTableProvider -> {
            try {
                List<Course> courses = mergedClassTableProvider.getTodayCourse();
                Collections.sort(courses, (o1, o2) -> o1.getArrange().get(0).getStart() - o2.getArrange().get(0).getStart());
                setupList(context, appWidgetId, remoteViews, (ArrayList) courses);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    private String getTodayString() {
        StringBuilder stringBuilder = new StringBuilder();
        Observable.just(Calendar.getInstance())
                .map(Calendar::getTime)
                .map(dateFormate::format)
                .subscribe(stringBuilder::append);
        stringBuilder.append("  ");
        String s = "星期" + TimeHelper.getChineseCharacter(CourseHelper.getTodayNumber());
        stringBuilder.append(s);
        return stringBuilder.toString();
    }
}
