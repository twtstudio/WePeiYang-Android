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
        //mContentList.get(5).setKV("时间");
        mContentList.get(6).setKV("课程安排","必修3-14");
        List<ClassTable.Data.Course.Arrange> arrangeList = course.arrange;
        int size = arrangeList.size();
        if (size == 1){
            ClassTable.Data.Course.Arrange arrange = arrangeList.get(0);
            mContentList.get(7).setKV("周数",arrange.week);
            mContentList.get(8).setKV("时间",arrange.start+"  "+arrange.end);
            mContentList.get(9).setKV("地点",arrange.room);
        }else if (size == 2){
            ClassTable.Data.Course.Arrange arrange = arrangeList.get(0);
            mContentList.get(7).setKV("周数",arrange.week);
            mContentList.get(8).setKV("时间",arrange.start+"  "+arrange.end);
            mContentList.get(9).setKV("地点",arrange.room);

            ClassTable.Data.Course.Arrange arrange2 = arrangeList.get(0);
            mContentList.get(10).setKV("周数",arrange2.week);
            mContentList.get(11).setKV("时间",arrange2.start+"  "+arrange2.end);
            mContentList.get(12).setKV("地点",arrange2.room);
        }
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

        public boolean isContent(){
            if (value != null){
                return true;
            }else {
                return false;
            }
        }
    }
}
