package com.twt.service.ui.news.comments;

import com.twt.service.bean.Comment;

import java.util.List;

/**
 * Created by sunjuntao on 16/2/14.
 */
public interface NewsCommentsView {

    void setSendBtnClickable(boolean clickable);

    void addComments(List<Comment> comments);

    void toastMessage(String message);

    void showProgress();

    void hideProgress();

    void clearEditText();
}
