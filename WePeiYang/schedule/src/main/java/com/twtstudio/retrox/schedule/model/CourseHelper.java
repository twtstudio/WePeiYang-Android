package com.twtstudio.retrox.schedule.model;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.twtstudio.retrox.schedule.R;
import com.twtstudio.retrox.schedule.TimeHelper;

import java.util.List;

/**
 * Created by retrox on 2017/2/6.
 */

public class CourseHelper {

    private static int[] classColors = new int[]{R.color.schedule_green,
            R.color.schedule_orange,
            R.color.schedule_blue,
            R.color.schedule_green2,
            R.color.schedule_pink,
            R.color.schedule_blue2,
            R.color.schedule_green3,
            R.color.schedule_purple,
            R.color.schedule_red,
            R.color.schedule_green4,
            R.color.schedule_purple2};

    public static List<ClassTable.Data.Course> getTodayCourses(ClassTable classTable) {
        long startUnix = Long.parseLong(classTable.data.term_start);
        int presentWeek = TimeHelper.getWeekInt(startUnix);
        List<ClassTable.Data.Course> courseList =
                Stream.of(classTable.data.data)
                        .peek(course -> course.isAvaiableCurrentWeek = checkAvaiablity(course.week.start, course.week.end, presentWeek))
                        .filter(course -> course.isAvaiableCurrentWeek)
                        .collect(Collectors.toList());
        for (int i = 0; i < courseList.size(); i++) {
            //反正也越界不了hhh
            courseList.get(i).coursecolor = classColors[i];
        }
        return courseList;
    }

    private static boolean checkAvaiablity(String start, String end, int week) {
        int startInt = Integer.parseInt(start);
        int endInt = Integer.parseInt(end);
        if (startInt <= week && endInt >= week) {
            return true;
        } else {
            return false;
        }
    }
}
