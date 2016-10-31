package com.twt.service.rxsrc.read.bookdetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.twt.service.R;
import com.twt.service.databinding.ActivityBookDetailBinding;
import com.twt.service.rxsrc.common.ui.BPActivity;
import com.twt.service.rxsrc.common.ui.BaseBindActivity;
import com.twt.service.rxsrc.model.read.Detail;

/**
 * Created by jcy on 16-10-25.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookDetailActivity extends BPActivity<BookDetailPresenter> implements BookDetailController,AdapterController{
    private ActivityBookDetailBinding mBinding;
    private RecyclerView mRecyclerView;
    private BookDetailAdapter mAdapter;
    private String id;

    @Override
    protected BookDetailPresenter getPresenter() {
        return new BookDetailPresenter(this,this);
    }

    @Override
    protected void actionStart(Context context) {
        id = getIntent().getStringExtra("id");
        super.actionStart(context);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_book_detail);
        //setUpToolbar(mBinding.readDetailToolbar);
        mRecyclerView = mBinding.bookDetailRcv;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BookDetailAdapter(this);
        mPresenter.getBookDetail(id);
        mBinding.bookDetailProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getStatusbarColor() {
        return R.color.text_secondary_color;
    }

    @Override
    public void onDetailGot(Detail detail) {
        mAdapter.setDetail(detail);
        mBinding.bookDetailProgressbar.setVisibility(View.INVISIBLE);
        if (mRecyclerView.getAdapter()==null){
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onReviewLike(String reviewId) {


    }

    @Override
    public void onBookLike(String bookId) {

    }


//    public void setFuckData(){
//        Detail detail = new Detail();
//    }

}
