package com.twt.service.ui.schedule;

import com.twt.service.bean.ClassTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcy on 16-9-23.
 */

public class ScheduleDetail {
    public List<Content> mContentList;

    public ScheduleDetail(ClassTable.Data.Course course) {
        mContentList = new ArrayList<>(15);
        for (int i = 0; i <15 ; i++) {
            mContentList.add(new Content());
        }
        mContentList.get(0).setKey("基本信息");
        mContentList.get(1).setKV("课程",course.coursename);
        mContentList.get(2).setKV("教师",course.teacher);
        mContentList.get(3).setKV("学分",course.credit);
        mContentList.get(4).setKV("类型",course.coursetype);
        //mContentList.get(5).setKV("时间",course.);
    }

    static class Content{
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setKV(String key,String value){
            this.key = key;
            this.value = value;
        }
    }
}
