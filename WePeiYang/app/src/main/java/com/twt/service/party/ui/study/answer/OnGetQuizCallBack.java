package com.twt.service.party.ui.study.answer;

import com.twt.service.party.bean.QuizInfo;

import java.util.List;

/**
 * Created by tjliqy on 2016/8/25.
 */
public interface OnGetQuizCallBack {
    void onGetQuizSuccess(List<QuizInfo.DataBean> dataList);
    void onGetQuizError(String msg);
    void onGetQuizFailure();
}
