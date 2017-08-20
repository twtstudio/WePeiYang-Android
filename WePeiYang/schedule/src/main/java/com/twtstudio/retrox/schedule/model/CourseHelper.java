package com.twtstudio.retrox.schedule.model;

import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.orhanobut.logger.Logger;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.twtstudio.retrox.schedule.R;
import com.twtstudio.retrox.schedule.TimeHelper;

import java.util.ArrayList;
import java.util.Calendar;
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

    private long startUnix;
    private static Calendar calendar = Calendar.getInstance();

    /**
     * 获取今天的课程
     *
     * @param classTable 传入classtable
     * @param adjust     是否用"无"填充并且去掉同名不同时课程
     * @return {分为普通课程和空课程} ,空课程属性 ：课程名为"无"，isAvaiableCurrentWeek = false; coursecolor =R.color.myWindowBackgroundGray;
     */
    public List<ClassTable.Data.Course> getTodayCourses(ClassTable classTable, boolean adjust) {
        startUnix = Long.parseLong(classTable.data.term_start);
        int presentWeek = TimeHelper.getWeekInt(startUnix, calendar);
        List<ClassTable.Data.Course> courseList =
                Stream.of(classTable.data.data)
//                        isAvailableCurrentWeek是检测课程是不是到期的，就是超出预计学习时间（周数）
                        .peek(course -> course.isAvaiableCurrentWeek = checkAvaiablity(course.week.start, course.week.end, presentWeek))
                        .filter(this::checkIsThisWeek)
                        .filter(this::checkIsToday)
                        .collect(Collectors.toList());

        for (int i = 0; i < courseList.size(); i++) {
            //反正也越界不了hhh
            if (!courseList.get(i).isAvaiableCurrentWeek) {
                courseList.get(i).coursecolor = R.color.myWindowBackgroundGray;
            } else {
                courseList.get(i).coursecolor = classColors[i];
            }
        }

        if (adjust) {
            return adjustCourseList(courseList);
        }
        courseList = Stream.of(courseList)
                .sorted((c1, c2) -> (getTodayStart(c1.arrange) - getTodayStart(c2.arrange)))
                .collect(Collectors.toList());
        return courseList;
    }

    public List<ClassTable.Data.Course> getTomorrowCourses(ClassTable classTable) {
        long mystartUnix = Long.parseLong(classTable.data.term_start);
        //计算明天的
        mystartUnix = mystartUnix - 24 * 60 * 60 * 1000;

        int presentWeek = TimeHelper.getWeekInt(mystartUnix, calendar);
        List<ClassTable.Data.Course> courseList =
                Stream.of(classTable.data.data)
//                        isAvailableCurrentWeek是检测课程是不是到期的，就是超出预计学习时间（周数）
                        .peek(course -> course.isAvaiableCurrentWeek = checkAvaiablity(course.week.start, course.week.end, presentWeek))
                        .filter(this::checkIsThisWeekForTomorrow)
                        .filter(this::checkIsTomorrow)
                        .collect(Collectors.toList());

        for (int i = 0; i < courseList.size(); i++) {
            //反正也越界不了hhh
            if (!courseList.get(i).isAvaiableCurrentWeek) {
                courseList.get(i).coursecolor = R.color.myWindowBackgroundGray;
            } else {
                courseList.get(i).coursecolor = classColors[i];
            }
        }

        courseList = Stream.of(courseList)
                .sorted((c1, c2) -> (getTomorrowStart(c1.arrange) - getTomorrowStart(c2.arrange)))
                .collect(Collectors.toList());

        return courseList;
    }

    private boolean checkIsThisWeekForTomorrow(ClassTable.Data.Course course) {
        int week = TimeHelper.getWeekInt(startUnix - 24 * 60 * 60 * 1000, calendar);
        for (ClassTable.Data.Course.Arrange arrange : course.arrange) {
            if (arrange.week.equals("单双周") ||
                    (arrange.week.equals("单周") && week % 2 == 1) ||
                    (arrange.week.equals("双周") && week % 2 == 0)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIsTomorrow(ClassTable.Data.Course course) {
        int today = getTomorrowNumber();
        for (ClassTable.Data.Course.Arrange arrange : course.arrange) {
            if (Integer.parseInt(arrange.day) == today) {
                return true;
            }
        }
        return false;
    }

    public static int getTomorrowNumber() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY) {
            return 1;
        } else {
            return (day);
        }
    }

    private boolean checkAvaiablity(String start, String end, int week) {
        int startInt = Integer.parseInt(start);
        int endInt = Integer.parseInt(end);
        if (startInt <= week && endInt >= week) {
            return true;
        } else {
            return false;
        }
    }

    public static int getTodayNumber() {
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY) {
            return 7;
        } else {
            return (day - 1);
        }
    }

    private boolean checkIsToday(ClassTable.Data.Course course) {
        int today = getTodayNumber();
        for (ClassTable.Data.Course.Arrange arrange : course.arrange) {
            if (Integer.parseInt(arrange.day) == today) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIsThisWeek(ClassTable.Data.Course course) {
        int week = TimeHelper.getWeekInt(startUnix, calendar);
        for (ClassTable.Data.Course.Arrange arrange : course.arrange) {
            if (course.isAvaiableCurrentWeek && (arrange.week.equals("单双周") ||
                    (arrange.week.equals("单周") && week % 2 == 1) ||
                    (arrange.week.equals("双周") && week % 2 == 0))) {
                return true;
            } else return false;
        }
        return false;
    }

    private List<ClassTable.Data.Course> adjustCourseList(List<ClassTable.Data.Course> courseList) {
        courseList = Stream.of(courseList)
                .sorted((c1, c2) -> (getTodayStart(c1.arrange) - getTodayStart(c2.arrange)))
                .collect(Collectors.toList());
        List<ClassTable.Data.Course> todayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ClassTable.Data.Course course1 = new ClassTable.Data.Course();
            course1.coursename = "无";
            course1.isAvaiableCurrentWeek = false;
            course1.coursecolor = com.twtstudio.retrox.schedule.R.color.myWindowBackgroundGray;
            todayList.add(course1);
        }
        for (ClassTable.Data.Course course : courseList) {
            int start = getTodayStart(course.arrange) / 2;
            int length = getTodayLength(course.arrange) / 2;
            if (todayList.get(start).coursename.equals("无")) {
                todayList.remove(start);
                todayList.add(start, course);
                if (length == 2) {
                    todayList.remove(start + 1);
                    todayList.add(start + 1, course);//某些四节课长的大课？没有测试
                }
            } else if (!todayList.get(start).isAvaiableCurrentWeek) {
                todayList.remove(start);
                todayList.add(start, course);
                if (length == 2) {
                    todayList.remove(start + 1);
                    todayList.add(start + 1, course);
                }
            }
        }
//        for (ClassTable.Data.Course course : todayList) {
//            if (course == null){
//                ClassTable.Data.Course course1 = new ClassTable.Data.Course();
//                course1.coursename = "无";
//                course1.isAvaiableCurrentWeek = false;
//            }
//        }

        return todayList;
    }

    public int getTodayStart(List<ClassTable.Data.Course.Arrange> courseArrange) {
        for (ClassTable.Data.Course.Arrange arrange : courseArrange) {
            if (Integer.parseInt(arrange.day) == getTodayNumber()) {
                return Integer.parseInt(arrange.start);
            }
        }
        return -1;
    }

    public int getTomorrowStart(List<ClassTable.Data.Course.Arrange> courseArrange) {
        for (ClassTable.Data.Course.Arrange arrange : courseArrange) {
            if (Integer.parseInt(arrange.day) == getTomorrowNumber()) {
                return Integer.parseInt(arrange.start);
            }
        }
        return -1;
    }


    public int getTodayEnd(List<ClassTable.Data.Course.Arrange> courseArrange) {
        for (ClassTable.Data.Course.Arrange arrange : courseArrange) {
            if (Integer.parseInt(arrange.day) == getTodayNumber()) {
                return Integer.parseInt(arrange.end);
            }
        }
        return -1;
    }

    private int getTodayLength(List<ClassTable.Data.Course.Arrange> courseArrange) {
        return getTodayStart(courseArrange) - getTodayEnd(courseArrange) + 1; //弄成2的倍数
    }

    public static String getTodayLocation(List<ClassTable.Data.Course.Arrange> courseArrange) {
        for (ClassTable.Data.Course.Arrange arrange : courseArrange) {
            if (Integer.parseInt(arrange.day) == getTodayNumber()) {
                return arrange.room;
            }
        }
        return "无法查询地点";
    }

    public static String getTomorrowLocation(List<ClassTable.Data.Course.Arrange> courseArrange) {
        for (ClassTable.Data.Course.Arrange arrange : courseArrange) {
            if (Integer.parseInt(arrange.day) == getTomorrowNumber()) {
                return arrange.room;
            }
        }
        return "无法查询地点";
    }

    public static String getTimeByStartInt(String start) {

        int s = Integer.parseInt(start);
        if (1 == s) {
            return "8:30";
        } else if (2 == s) {
            return "9:20";
        } else if (3 == s) {
            return "10:25";
        } else if (4 == s) {
            return "11:15";
        } else if (5 == s) {
            return "1:30";
        } else if (6 == s) {
            return "2:20";
        } else if (7 == s) {
            return "3:25";
        } else if (8 == s) {
            return "4:15";
        } else if (9 == s) {
            return "6:30";
        } else if (10 == s) {
            return "7:20";
        } else {
            return "";
        }
    }

    public static void setCalendar(CalendarDay calendarDay) {
        calendarDay.copyTo(calendar);
    }
}
