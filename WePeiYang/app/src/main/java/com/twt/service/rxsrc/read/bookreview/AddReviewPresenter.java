package com.twt.service.rxsrc.read.bookreview;

import android.content.Context;

import com.twt.service.rxsrc.common.Presenter;

/**
 * Created by jcy on 16-10-28.
 *
 * @TwtStudio Mobile Develope Team
 */

public class AddReviewPresenter extends Presenter {
    private AddReviewController mController;

    public AddReviewPresenter(Context context, AddReviewController controller) {
        super(context);
        mController = controller;

    }

    public void addReview(String review){

    }
}
