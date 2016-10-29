package com.twt.service.rxsrc.read.home.profile;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.twt.service.R;
import com.twt.service.rxsrc.common.ui.BaseFragment;
import com.twt.service.rxsrc.model.read.MyBookShelf;
import com.twt.service.rxsrc.model.read.Review;
import com.twt.service.rxsrc.read.home.BookReviewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tjliqy on 2016/10/28.
 */

public class BookProfileFragment extends BaseFragment {
    @InjectView(R.id.iv_delete)
    ImageView mIvDelete;
    @InjectView(R.id.rv_collect)
    RecyclerView mRvCollect;
    @InjectView(R.id.rv_review)
    RecyclerView mRvReview;

    private BookReviewAdapter mReviewAdapter;
    private BookShelfAdapter mShelfAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_book_profile;
    }

    @Override
    protected void initView() {

        mShelfAdapter = new BookShelfAdapter(getContext());
        mShelfAdapter.hideFooter();
        mRvCollect.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCollect.setAdapter(mShelfAdapter);


        mReviewAdapter = new BookReviewAdapter(getContext());
        mReviewAdapter.hideFooter();
        mRvReview.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvReview.setAdapter(mReviewAdapter);


        // TODO: 2016/10/28 测试代码


        List<MyBookShelf> books = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            books.add(new MyBookShelf());
        }
        mShelfAdapter.addItems(books);


        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            reviews.add(new Review());
        }
        mReviewAdapter.addItems(reviews);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
