package com.twtstudio.retrox.schedule.model;

import com.twt.wepeiyang.commons.network.ApiException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunjuntao on 15/11/5.
 */
public class ClassTable {
    public int error_code;
    public String message;
    public Data data;

    public static class Data {
        public String term;
        public List<Course> data;
        public int week;
        public String updated_at;
        public String term_start;

        public static class Course implements Serializable{

            private static final long serialVersionUID = -4083503801443301445L;

            public String classid;//班级编号
            public String courseid;//课程编号
            public String coursename;//课程名称
            public String coursetype;//课程类型，如『学科基础』
            public String coursenature;//必修or选修
            public String credit;//学分
            public String teacher;//授课教师
            public List<Arrange> arrange;
            public int coursecolor;//课程表的颜色
            public boolean isAvaiableCurrentWeek = false;

            public static class Arrange implements Serializable {
                public String week;//单双周
                public String day;//周几上课
                public String start;//开始于第几节课
                public String end;//结束于第几节课
                public String room;//上课地点
            }


            public Week week;//从第几周至第几周上课

            public static class Week implements Serializable {
                public String start;//开始周数
                public String end;//结束周数
            }

            public String college;//开课学院
            public String campus;//开课校区
            public String ext;//是否为重修
        }
    }

    public Data getData() {
        if (error_code == -1){
            return data;
        }else {
            throw new ApiException(error_code,message);
        }
    }
}
