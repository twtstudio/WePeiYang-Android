package cn.edu.tju.examtable.service

import com.twt.wepeiyang.commons.experimental.preference.hawk

object ExamtablePreference {
    var exams: Array<ExamBean> by hawk("exam_key", arrayOf())
}
