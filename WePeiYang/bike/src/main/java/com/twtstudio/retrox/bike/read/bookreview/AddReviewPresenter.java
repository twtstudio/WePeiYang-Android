package com.twtstudio.retrox.bike.read.bookreview;

import android.content.Context;

import com.twtstudio.retrox.bike.api.OnNextListener;
import com.twtstudio.retrox.bike.api.ReadApiClient;
import com.twtstudio.retrox.bike.api.ReadApiSubscriber;
import com.twtstudio.retrox.bike.common.Presenter;
import com.twtstudio.retrox.bike.model.read.ReviewCallback;


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
