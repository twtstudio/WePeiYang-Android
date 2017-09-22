package com.twt.service.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.orhanobut.hawk.Hawk;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.CourseHelper;
import com.twt.service.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by retrox on 26/03/2017.
 */

public class WidgetListFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<ClassTable.Data.Course> courseList;
    private final CourseHelper helper = new CourseHelper();

    public WidgetListFactory(Context mContext, List<ClassTable.Data.Course> courses) {
        this.mContext = mContext;
        courseList = courses;
    }

    @Override
    public void onCreate() {
//        getData(false);
        CourseHelper.setCalendar(CalendarDay.today());
//        if (courseList.size() == 0) {
//            ClassTable.Data.Course course = new ClassTable.Data.Course();
//            course.coursename = "今天没课！！！";
//            courseList.add(course);
//        }
    }

    @Override
    public void onDataSetChanged() {
//        getData(false);
        courseList = Hawk.get("scheduleCache", new ArrayList<ClassTable.Data.Course>());
    }

    @Override
    public void onDestroy() {
        courseList.clear();
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_schedule_item);

        ClassTable.Data.Course course = courseList.get(position);
        remoteViews.setTextViewText(R.id.widget_course_title, course.coursename);
        if (course.coursename.equals("今天没课！！！")) {
            remoteViews.setViewVisibility(R.id.widget_course_location_icon, View.VISIBLE);
        }
        if (!(course.coursename.equals("无") || course.coursename.equals("今天没课！！！"))) {

            String location = CourseHelper.getTodayLocation(course.arrange);
            remoteViews.setTextViewText(R.id.widget_course_location, location);
            String time = "第" + helper.getTodayStart(course.arrange) + "-" + helper.getTodayEnd(course.arrange) + "节";
            remoteViews.setTextViewText(R.id.widget_course_time, time);

        }

        Intent fillInIntent = new Intent();
//        fillInIntent.putExtra()
        remoteViews.setOnClickFillInIntent(R.id.widget_schedule_item, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
