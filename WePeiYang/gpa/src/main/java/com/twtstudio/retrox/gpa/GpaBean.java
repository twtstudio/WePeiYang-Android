package com.twtstudio.retrox.gpa;

import java.io.Serializable;
import java.util.List;

/**
 * Created by retrox on 2017/1/17.
 */

public class GpaBean implements Serializable {

    public Stat stat;//整体分数情况
    public List<Term> data;//各学期列表
    public String updated_at;//更新时间
    public String session;

    public class Stat {
        public List<Year> years;//各学年分数列表
        public Total total;//总体分数列表
        // public List<Object> double;

        public class Year {
            public String year;//学年
            public double score;//该学年加权分数
            public double gpa;//该学年gpa
            public double credit;//该学年学分
        }

        public class Total {
            public double score;//总加权分数
            public double gpa;//总gpa
            public double credit;//总学分
        }
    }

    public class Term {
        public String term;//学期编号
        public List<Course> data;//课程列表
        public String name;//学期名称
        public TermStat stat;

        public class Course implements Serializable {
            public String no;//课程编号
            public String name;//课程名称
            public int type;//课程类型
            public double credit;//课程学分
            public int reset;//实在猜不出来
            public double score;
            public Evaluate evaluate;

            public class Evaluate implements Serializable {
                public String lesson_id;
                public String term;
                public String union_id;
                public String course_id;
            }
        }

        public class TermStat {
            public double score;//该学期加权成绩
            public double gpa;//该学期gpa
            public double credit;//该学期学分
        }
    }


}
