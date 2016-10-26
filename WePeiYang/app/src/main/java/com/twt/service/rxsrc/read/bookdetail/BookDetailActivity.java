package com.twt.service.rxsrc.read.bookdetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

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

public class BookDetailActivity extends BPActivity<BookDetailPresenter> implements BookDetailController{
    private ActivityBookDetailBinding mBinding;
    private RecyclerView mRecyclerView;

    @Override
    protected BookDetailPresenter getPresenter() {
        return new BookDetailPresenter(this,this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_book_detail);
        setUpToolbar(mBinding.readDetailToolbar);
        mRecyclerView = mBinding.bookDetailRcv;
    }

    @Override
    protected int getStatusbarColor() {
        return 0;
    }

    @Override
    public void onDetailGot(Detail detail) {

    }
}
