package com.twt.service.push;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.schedule.model.ClassTable;
import com.twtstudio.retrox.schedule.model.ClassTableProvider;
import com.twtstudio.retrox.schedule.model.CourseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by retrox on 2017/2/23.
 */

public class PushProvider {
    private RxAppCompatActivity activity;
    private final CourseHelper courseHelper = new CourseHelper();

    public PushProvider(RxAppCompatActivity activity) {
        this.activity = activity;
    }

    public void queryCourseMessage(Action1<CoursePushBean> action1) {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);

        if (hours <= 9) {
            ClassTableProvider.init(activity)
                    .registerAction(classTable -> {
                        List<ClassTable.Data.Course> courseList = trimCourses(new CourseHelper().getTodayCourses(classTable, true));
                        if (courseList.size() == 0) {
                            action1.call(new CoursePushBean("今日课程", "今天没课！\n做点有趣的事情吧"));
                        } else {
                            ClassTable.Data.Course course = courseList.get(0);
                            action1.call(new CoursePushBean("今日课程", "共" + courseList.size() + "门\n" +
                                    "第一门是" + course.coursename + "\n在第" + courseHelper.getTodayStart(course.arrange) + "节开始\n" +
                                    "位置：" + CourseHelper.getTodayLocation(course.arrange)));
                        }
                    }).getData(CalendarDay.today());
        } else if (hours >= 21) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            // FIXME: 2017/2/24 秘制明日课程问题
            ClassTableProvider.init(activity)
                    .registerAction(classTable -> {
                        final CourseHelper courseHelper = new CourseHelper();
                        courseHelper.setCalendar(CalendarDay.from(calendar));
                        List<ClassTable.Data.Course> courses = courseHelper.getTodayCourses(classTable, false);
                        List<ClassTable.Data.Course> courseList = trimCourses(courses);
                        if (courseList.size() == 0) {
                            action1.call(new CoursePushBean("明日课程", "明天没课！\n做点有趣的事情吧"));
                        } else {
                            ClassTable.Data.Course course = courseList.get(0);
                            action1.call(new CoursePushBean("明日课程", "共" + courseList.size() + "门\n" +
                                    "第一门是" + course.coursename + "\n在第" + courseHelper.getTomorrowStart(course.arrange) + "节开始\n" +
                                    "位置：" + CourseHelper.getTomorrowLocation(course.arrange)));
                        }
                    }).getData(CalendarDay.from(calendar));
        }

    }

    private List<ClassTable.Data.Course> trimCourses(List<ClassTable.Data.Course> courseList) {
        List<ClassTable.Data.Course> courses = new ArrayList<>();
        for (ClassTable.Data.Course course : courseList) {
            if (course.isAvaiableCurrentWeek) {
                courses.add(course);
            }
        }
        return courses;
    }

    public static class CoursePushBean {
        public String title;
        public String message;

        public CoursePushBean() {
        }

        public CoursePushBean(String title, String message) {
            this.title = title;
            this.message = message;
        }
    }
}
