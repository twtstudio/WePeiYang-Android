package com.twt.service.rxsrc.read.home.recommend;

import android.content.Context;

import com.twt.service.rxsrc.api.OnNextListener;
import com.twt.service.rxsrc.api.ReadApiClient;
import com.twt.service.rxsrc.api.ReadApiSubscriber;
import com.twt.service.rxsrc.common.Presenter;
import com.twt.service.rxsrc.model.read.HomeBanner;
import com.twt.service.rxsrc.model.read.Recommended;
import com.twt.service.rxsrc.model.read.Review;
import com.twt.service.rxsrc.model.read.User;
import com.youth.banner.Banner;

import java.util.List;

/**
 * Created by tjliqy on 2016/10/27.
 */

public class BookRecommendPresenter extends Presenter {

    private BookRecommendController mController;

    public BookRecommendPresenter(Context context, BookRecommendController controller) {
        super(context);
        mController = controller;
    }

    public void getBanner(){
        ReadApiClient.getInstance().getBanner(mContext, new ReadApiSubscriber(mContext,mBannerOnNextListener));
    }

    public void getRecommendedList(String count){
        ReadApiClient.getInstance().getRecommendedList(mContext, new ReadApiSubscriber(mContext,mRecommendedOnNextListener),count);
    }

    public void getReviewList(String count){
        ReadApiClient.getInstance().getReviewList(mContext, new ReadApiSubscriber(mContext, mReviewOnNextListener), count);
    }

    public void addLike(String id){
        ReadApiClient.getInstance().addLike(mContext, new ReadApiSubscriber(mContext, null), id);
    }

    public void delLike(String id){
        ReadApiClient.getInstance().delLike(mContext, new ReadApiSubscriber(mContext, null), id);
    }

    public void getStarReaderList(String count){
        ReadApiClient.getInstance().getStarReaderList(mContext, new ReadApiSubscriber(mContext, mStarReaderOnNextListener), count);
    }
    private OnNextListener<List<HomeBanner>> mBannerOnNextListener = new OnNextListener<List<HomeBanner>>() {
        @Override
        public void onNext(List<HomeBanner> banners) {
            mController.bindBannerData(banners);
        }
    };

    private OnNextListener<List<Recommended>> mRecommendedOnNextListener = new OnNextListener<List<Recommended>>() {
        @Override
        public void onNext(List<Recommended> recommends) {
            mController.bindRecommendedData(recommends);
        }
    };

    private OnNextListener<List<Review>> mReviewOnNextListener = new OnNextListener<List<Review>>() {
        @Override
        public void onNext(List<Review> reviews) {
            mController.bindReviewData(reviews);
        }
    };

    private OnNextListener<List<User>> mStarReaderOnNextListener = new OnNextListener<List<User>>() {
        @Override
        public void onNext(List<User> users) {
            mController.bindStarReaderData(users);
        }
    };
}
