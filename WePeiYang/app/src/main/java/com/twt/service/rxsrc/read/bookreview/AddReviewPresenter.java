package com.twt.service.rxsrc.read.bookreview;

import android.content.Context;
import android.widget.Toast;

import com.twt.service.rxsrc.api.OnNextListener;
import com.twt.service.rxsrc.api.ReadApiClient;
import com.twt.service.rxsrc.api.ReadApiSubscriber;
import com.twt.service.rxsrc.common.Presenter;
import com.twt.service.rxsrc.model.read.ReviewCallback;

/**
 * Created by jcy on 16-10-28.
 *
 * @TwtStudio Mobile Develop Team
 */

public class AddReviewPresenter extends Presenter {
    private AddReviewController mController;

    public AddReviewPresenter(Context context, AddReviewController controller) {
        super(context);
        mController = controller;

    }

    public void addReview(String id,String review,float star){
        int intstar = (int) star;
        ReadApiClient.getInstance().addReview(mContext,new ReadApiSubscriber(mContext,mReviewCallbackListener),id,review,intstar);
    }

    OnNextListener<ReviewCallback> mReviewCallbackListener = new OnNextListener<ReviewCallback>() {
        @Override
        public void onNext(ReviewCallback reviewCallback) {
            mController.onAddFinished(reviewCallback);
        }
    };
}
