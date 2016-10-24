package com.twt.service.rxsrc.read.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.twt.service.R;
import com.twt.service.databinding.ActivityBookSearchBinding;
import com.twt.service.rxsrc.common.ui.BPActivity;
import com.twt.service.rxsrc.common.ui.PActivity;
import com.twt.service.rxsrc.model.read.SearchBook;

import java.util.List;

/**
 * Created by jcy on 16-10-23.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookSearchActivity extends BPActivity<BookSearchPresenter> implements BookSearchViewController{

    private ActivityBookSearchBinding mBinding;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;

    @Override
    protected BookSearchPresenter getPresenter() {
        return new BookSearchPresenter(this,this);
    }

    @Override
    protected void actionStart(Context context) {

    }

    @Override
    protected int getStatusbarColor() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_book_search);
        mBinding.setHint("输入你想查找的书名");
        mSearchView = mBinding.readSearchView;
        mRecyclerView = mBinding.readSearchRecyclerview;
        mToolbar = mBinding.readSearchToolbar;
    }

    @Override
    protected void afterInitView() {
        super.afterInitView();
        setUpToolbar(mToolbar);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onSearchFinished(List<SearchBook> bookList) {

    }
}
