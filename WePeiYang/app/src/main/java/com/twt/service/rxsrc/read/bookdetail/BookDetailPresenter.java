package com.twt.service.rxsrc.read.bookdetail;

import android.content.Context;
import android.widget.Toast;

import com.twt.service.rxsrc.api.OnNextListener;
import com.twt.service.rxsrc.api.ReadApiClient;
import com.twt.service.rxsrc.api.ReadApiSubscriber;
import com.twt.service.rxsrc.common.Presenter;
import com.twt.service.rxsrc.model.read.Detail;
import com.twt.service.rxsrc.model.read.RefreshEvent;
import com.twt.service.rxsrc.model.read.Review;

import de.greenrobot.event.EventBus;

/**
 * Created by jcy on 16-10-25.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookDetailPresenter extends Presenter {

    private BookDetailController mController;

    public BookDetailPresenter(Context context, BookDetailController controller) {
        super(context);
        mController = controller;
    }

    public void getBookDetail(String id) {
        ReadApiClient.getInstance().getBookDetail(mContext, new ReadApiSubscriber(mContext, mDetailOnNextListener), id);
}

    private OnNextListener<Detail> mDetailOnNextListener = new OnNextListener<Detail>() {
        @Override
        public void onNext(Detail detail) {
            mController.onDetailGot(detail);
        }
    };

    public void loveReview(String reviewId) {
        ReadApiClient.getInstance().addLike(mContext, new ReadApiSubscriber(mContext, mOnLoveListener), reviewId);
    }

    private OnNextListener<Integer> mOnLoveListener = new OnNextListener<Integer>() {
        @Override
        public void onNext(Integer integer) {
            Toast.makeText(mContext, "已点赞", Toast.LENGTH_SHORT).show();
        }
    };

    public void loveBook(String bookId) {
        ReadApiClient.getInstance().addBookShelf(mContext, new ReadApiSubscriber(mContext, mLoveBookListener), bookId);
    }

    private OnNextListener<Object> mLoveBookListener = new OnNextListener<Object>() {
        @Override
        public void onNext(Object o) {
            Toast.makeText(mContext, "已收藏", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new RefreshEvent());
        }
    };
}
