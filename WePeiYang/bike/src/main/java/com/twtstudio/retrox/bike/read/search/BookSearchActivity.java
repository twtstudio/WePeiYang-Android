package com.twtstudio.retrox.bike.read.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.common.ui.BPActivity;
import com.twtstudio.retrox.bike.databinding.ActivityBookSearchBinding;
import com.twtstudio.retrox.bike.model.read.SearchBook;

import java.util.List;



/**
 * Created by jcy on 16-10-23.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookSearchActivity extends BPActivity<BookSearchPresenter> implements BookSearchViewController {

    private ActivityBookSearchBinding mBinding;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private BookSearchAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected BookSearchPresenter getPresenter() {
        return new BookSearchPresenter(this, this);
    }

    @Override
    protected void actionStart(Context context) {

    }

    @Override
    protected int getStatusbarColor() {
        return R.color.read_primary_color;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_book_search);
        mBinding.setHint("输入你想查找的书名");
        mProgressBar = mBinding.bookSearchProgressbar;
        mSearchView = mBinding.readSearchView;
        mSearchView.setIconifiedByDefault(false);
        mRecyclerView = mBinding.readSearchRecyclerview;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mToolbar = mBinding.readSearchToolbar;
        mAdapter = new BookSearchAdapter(this);

        setTitle("搜索图书");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.edittext_bg_color));

        setUpToolbar(mToolbar);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.search(query);
                mProgressBar.setVisibility(View.VISIBLE);
                mSearchView.setSubmitButtonEnabled(false);
//                mSearchView
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm!=null){
                    imm.hideSoftInputFromWindow(mSearchView.getWindowToken(),0);
                }
                mSearchView.clearFocus();
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
        if (bookList.size() == 0){
            toastMessage("找不到书目哦..");
        }
        mSearchView.setSubmitButtonEnabled(true);
        mProgressBar.setVisibility(View.GONE);
        mAdapter.refreshItems(bookList);
        mRecyclerView.setAdapter(mAdapter);

    }
}