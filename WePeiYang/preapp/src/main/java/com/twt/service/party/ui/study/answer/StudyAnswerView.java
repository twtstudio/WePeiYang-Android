package com.twt.service.party.ui.study.answer;

import com.twt.service.party.bean.QuizInfo;

import java.util.List;

/**
 * Created by tjliqy on 2016/8/25.
 */
public interface StudyAnswerView {
    void bindData(List<QuizInfo.DataBean> dataList);
    void setError(String msg);
    void toastMsg(String msg);
}
