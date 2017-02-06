package com.twtstudio.retrox.schedule;

import com.twtstudio.retrox.schedule.model.ClassTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcy on 16-9-23.
 */

public class ScheduleDetail {
    public List<Content> mContentList;

    public ScheduleDetail(ClassTable.Data.Course course) {
        mContentList = new ArrayList<>();
        mContentList.add(new Content().setKey("基本信息"));
        mContentList.add(new Content().setKV("课程",course.coursename));
        mContentList.add(new Content().setKV("教师",course.teacher));
        mContentList.add(new Content().setKV("学分",course.credit));
        mContentList.add(new Content().setKV("类型",course.coursetype));
        mContentList.add(new Content().setKey("课程详情"));
        mContentList.add(new Content().setKV("课程安排",course.coursenature+" 第"+course.week.start+"-"+course.week.end+"周"));
        List<ClassTable.Data.Course.Arrange> arrangeList = course.arrange;
        int size = arrangeList.size();
        if (size == 1){
            ClassTable.Data.Course.Arrange arrange = arrangeList.get(0);
            mContentList.add(new Content().setKV("周数",arrange.week+" 周"+TimeHelper.getWeekString(Integer.parseInt(arrange.day))));
            mContentList.add(new Content().setKV("时间","第"+arrange.start+"-"+arrange.end+"节"));
            mContentList.add(new Content().setKV("地点",arrange.room));
        }else if (size == 2){
            ClassTable.Data.Course.Arrange arrange = arrangeList.get(0);
            mContentList.add(new Content().setKV("周数",arrange.week+" 周"+TimeHelper.getWeekString(Integer.parseInt(arrange.day))));
            mContentList.add(new Content().setKV("时间","第"+arrange.start+"-"+arrange.end+"节"));
            mContentList.add(new Content().setKV("地点",arrange.room));

            mContentList.add(new Content().setKey("  "));
            ClassTable.Data.Course.Arrange arrange2 = arrangeList.get(1);
            mContentList.add(new Content().setKV("周数",arrange2.week+" 周"+TimeHelper.getWeekString(Integer.parseInt(arrange2.day))));
            mContentList.add(new Content().setKV("时间","第"+arrange2.start+"-"+arrange2.end+"节"));
            mContentList.add(new Content().setKV("地点",arrange2.room));
        }
        mContentList.add(new Content().setKey(" "));
        //mContentList.get(5).setKV("时间",course.);
    }

    static class Content{
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public Content setKey(String key) {
            this.key = key;
            return this;
        }

        public String getValue() {
            return value;
        }

        public Content setValue(String value) {
            this.value = value;
            return this;
        }

        public Content setKV(String key,String value){
            this.key = key;
            this.value = value;
            return this;
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
