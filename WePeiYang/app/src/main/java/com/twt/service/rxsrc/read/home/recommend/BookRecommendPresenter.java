package com.twt.service.rxsrc.read.home.recommend;

import android.content.Context;

import com.twt.service.rxsrc.common.Presenter;

/**
 * Created by tjliqy on 2016/10/27.
 */

public class BookRecommendPresenter extends Presenter {

    private BookRecommendController mController;
    public BookRecommendPresenter(Context context, BookRecommendController controller) {
        super(context);
        mController = controller;
    }
}
