package com.twt.service.rxsrc.read.home.recommend;

import android.content.Context;
import android.databinding.tool.util.L;

import com.twt.service.rxsrc.api.OnNextListener;
import com.twt.service.rxsrc.api.ReadApiClient;
import com.twt.service.rxsrc.api.ReadApiSubscriber;
import com.twt.service.rxsrc.common.Presenter;
import com.twt.service.rxsrc.model.read.HomeBanner;
import com.twt.service.rxsrc.model.read.ReadToken;
import com.twt.service.rxsrc.model.read.Recommended;
import com.twt.service.rxsrc.model.read.Review;
import com.twt.service.rxsrc.model.read.User;
import com.twt.service.support.PrefUtils;
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

    public void getToken(String wpyToken) {
        ReadApiClient.getInstance().getReadToken(mContext, new ReadApiSubscriber(mContext, mTokenOnNextListener), wpyToken);
    }

    public void getBanner() {
        ReadApiClient.getInstance().getBanner(mContext, new ReadApiSubscriber(mContext, mBannerOnNextListener));
    }

    public void getRecommendedList(String count) {
        ReadApiClient.getInstance().getRecommendedList(mContext, new ReadApiSubscriber(mContext, mRecommendedOnNextListener), count);
    }

    public void getReviewList(String count) {
        ReadApiClient.getInstance().getReviewList(mContext, new ReadApiSubscriber(mContext, mReviewOnNextListener), count);
    }

    public void addLike(String id) {
        ReadApiClient.getInstance().addLike(mContext, new ReadApiSubscriber(mContext, null), id);
    }

    public void delLike(String id) {
        ReadApiClient.getInstance().delLike(mContext, new ReadApiSubscriber(mContext, null), id);
    }

    public void getStarReaderList(String count) {
        ReadApiClient.getInstance().getStarReaderList(mContext, new ReadApiSubscriber(mContext, mStarReaderOnNextListener), count);
    }

    private OnNextListener<List<HomeBanner>> mBannerOnNextListener = new OnNextListener<List<HomeBanner>>() {
        @Override
        public void onNext(List<HomeBanner> banners) {
            mController.bindBannerData(banners);
        }
    };

    private OnNextListener<List<Recommended>> mRecommendedOnNextListener = recommends -> mController.bindRecommendedData(recommends);

    private OnNextListener<ReadToken> mTokenOnNextListener = readTokens -> {
        PrefUtils.setReadToken(readTokens.token);
        mController.onGetTokenSuccess();
    };


    private OnNextListener<List<Review>> mReviewOnNextListener = reviews -> mController.bindReviewData(reviews);

    private OnNextListener<List<User>> mStarReaderOnNextListener = users -> mController.bindStarReaderData(users);
}
