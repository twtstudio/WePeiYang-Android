package com.twt.service.rxsrc.read.home.profile;

import android.content.Context;
import android.databinding.tool.util.L;

import com.twt.service.rxsrc.api.OnNextListener;
import com.twt.service.rxsrc.api.ReadApiClient;
import com.twt.service.rxsrc.api.ReadApiSubscriber;
import com.twt.service.rxsrc.common.Presenter;
import com.twt.service.rxsrc.model.read.BookInShelf;
import com.twt.service.rxsrc.model.read.Review;

import java.util.List;

/**
 * Created by tjliqy on 2016/11/1.
 */

public class BookProfilePresenter extends Presenter {

    private BookProfileController mController;

    public BookProfilePresenter(Context context, BookProfileController controller) {
        super(context);
        mController = controller;
    }

    public void getBookShelf(){
        ReadApiClient.getInstance().getBookShelf(mContext, new ReadApiSubscriber(mContext, mBookShelfOnNextListener));
    }

    public void delBookShelf(String[] id){
        ReadApiClient.getInstance().delBookInShelf(mContext, new ReadApiSubscriber(mContext, mDelBookInSelfListener), id);
    }

    public void getMyReview(){
        ReadApiClient.getInstance().getMyReview(mContext, new ReadApiSubscriber(mContext, mReviewListener));
    }

    public void addLike(String id){
        ReadApiClient.getInstance().addLike(mContext, new ReadApiSubscriber(mContext, null), id);
    }

    public void delLike(String id){
        ReadApiClient.getInstance().delLike(mContext, new ReadApiSubscriber(mContext, null), id);
    }

    private OnNextListener<List<BookInShelf>> mBookShelfOnNextListener = new OnNextListener<List<BookInShelf>>() {
        @Override
        public void onNext(List<BookInShelf> booksInShelf) {
            mController.bindBookShelfData(booksInShelf);
        }
    };

    private OnNextListener<Object> mDelBookInSelfListener = new OnNextListener<Object>() {
        @Override
        public void onNext(Object o) {
            mController.delBookInShelfSuccess();
        }
    };

    private OnNextListener<List<Review>> mReviewListener = new OnNextListener<List<Review>>() {
        @Override
        public void onNext(List<Review> reviews) {
            mController.bindReviewData(reviews);
        }
    };
}
