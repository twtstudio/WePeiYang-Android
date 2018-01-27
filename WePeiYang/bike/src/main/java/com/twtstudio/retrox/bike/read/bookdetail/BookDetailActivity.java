package com.twtstudio.retrox.bike.read.bookdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;


import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.common.ui.BPActivity;
import com.twtstudio.retrox.bike.model.read.Detail;

/**
 * Created by jcy on 16-10-25.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookDetailActivity extends BPActivity<BookDetailPresenter> implements BookDetailController, AdapterController {
    private RecyclerView mRecyclerView;
    private BookDetailAdapter mAdapter;
    private ProgressBar mProgressBar;
    private String id;

    @Override
    protected BookDetailPresenter getPresenter() {
        return new BookDetailPresenter(this, this);
    }

    @Override
    protected void actionStart(Context context) {
        id = getIntent().getStringExtra("id");
        super.actionStart(context);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        mProgressBar = (ProgressBar) findViewById(R.id.book_detail_progressbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.book_detail_rcv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BookDetailAdapter(this);
        mPresenter.getBookDetail(id);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getStatusbarColor() {
        return R.color.text_secondary_color;
    }

    @Override
    public void onDetailGot(Detail detail) {
        mAdapter.setDetail(detail);
        mProgressBar.setVisibility(View.INVISIBLE);
        if (mRecyclerView.getAdapter() == null) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onReviewLike(String reviewId) {
        mPresenter.loveReview(reviewId);

    }

    @Override
    public void onBookLike(String bookId) {
        mPresenter.loveBook(bookId);
    }


//    public void setFuckData(){
//        Detail detail = new Detail();
//    }

}
