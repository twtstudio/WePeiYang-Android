package com.twt.service.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.twt.wepeiyang.commons.cache.CacheProvider;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.schedule.ScheduleActivity;
import com.twtstudio.retrox.schedule.TimeHelper;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.CourseHelper;
import com.twtstudio.retrox.schedule.model.ScheduleApi;
import com.twtstudio.retrox.schedule.model.ScheduleCacheApi;
import com.twt.service.R;
import com.twt.service.home.HomeActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by retrox on 24/03/2017.
 */

public class ScheduleWidgetProvider extends AppWidgetProvider {

    private final CourseHelper helper = new CourseHelper();
    public static final SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
    private AppWidgetManager manager = null;


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("com.twt.appwidget.refresh")) {
            ComponentName name = new ComponentName(context, ScheduleWidgetProvider.class);
            this.onUpdate(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context).getAppWidgetIds(name));
            Logger.d("widget refresh click!");
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        CourseHelper.setCalendar(CalendarDay.today());
        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_schedule);
            remoteViews.setOnClickPendingIntent(R.id.widget_framelayout, pendingIntent);
//            remoteViews.setOnClickPendingIntent(R.id.widget_listview,pendingIntent);

            Intent imageClickIntent = new Intent("com.twt.appwidget.refresh");
            PendingIntent imageClickPendingIntent = PendingIntent.getBroadcast(context, 0, imageClickIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_image_button, imageClickPendingIntent);

            remoteViews.setTextViewText(R.id.widget_today_date, getTodayString());

            getData(context, appWidgetId, remoteViews, false);

            manager = appWidgetManager;
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        }
    }

    private void setupList(Context context, int appWidgetId, RemoteViews remoteViews, ArrayList list) {
        Intent serviceIntent = new Intent(context, WidgetService.class);

        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        Hawk.put("scheduleCache", list);

        Intent startActivityIntent = new Intent(context, ScheduleActivity.class);
        PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_listview, startActivityPendingIntent);

        remoteViews.setRemoteAdapter(R.id.widget_listview, serviceIntent);
        remoteViews.setEmptyView(R.id.widget_listview, R.id.widget_empty_list);

        manager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview);
        manager.updateAppWidget(appWidgetId, remoteViews);

    }

    public void getData(Context context, int appWidgetId, RemoteViews remoteViews, boolean update) {

        CacheProvider.getRxCache().using(ScheduleCacheApi.class)
                .getClassTableAuto(RetrofitProvider.getRetrofit().create(ScheduleApi.class)
                        .getClassTable(), new DynamicKey("classTable"), new EvictDynamicKey(update))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(reply -> Logger.d(reply.toString()))
                .map(Reply::getData)
                .subscribe(classTable -> {
                    //存入学期开始时间
                    CommonPrefUtil.setStartUnix(Long.valueOf(classTable.data.term_start));

                    helper.setCalendar(CalendarDay.today());
                    List<ClassTable.Data.Course> courseList = helper.getTodayCourses(classTable, true);
                    //去除无用课程
                    courseList = Stream.of(courseList)
                            .filter(course -> course.isAvaiableCurrentWeek)
                            .filter(course -> !course.coursename.equals("无"))
                            .collect(Collectors.toList());

                    setupList(context, appWidgetId, remoteViews, (ArrayList) courseList);

                }, throwable -> new RxErrorHandler().call(throwable.getCause()));

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
