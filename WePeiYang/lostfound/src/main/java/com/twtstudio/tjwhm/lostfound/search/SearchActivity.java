package com.twtstudio.tjwhm.lostfound.search;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.R2;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;
import com.twtstudio.tjwhm.lostfound.waterfall.WaterfallBean;
import com.twtstudio.tjwhm.lostfound.waterfall.WaterfallTableAdapter;

import java.util.ArrayList;

import butterknife.BindView;

import static com.facebook.common.internal.Ints.max;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class SearchActivity extends BaseActivity implements SearchContract.SearchUIView {

    private InputMethodManager inputMethodManager;
    private WaterfallBean waterfallBean;
    private WaterfallTableAdapter tableAdapter;
    private StaggeredGridLayoutManager layoutManager;
    SearchContract.SearchPresenter searchPresenter;

    @BindView(R2.id.search_toolbar)
    Toolbar toolbar;
    @BindView(R2.id.search_searview)
    SearchView searchView;
    @BindView(R2.id.search_recyclerView)
    RecyclerView search_recyclerView;
    @BindView(R2.id.search_progress)
    ProgressBar search_progress;
    @BindView(R2.id.search_no_res)
    LinearLayout search_no_res;

    int page = 1;
    String keyword;
    boolean isLoading = false;


    @Override
    protected int getLayoutResourceId() {
        return R.layout.lf_activity_search;
    }

    @Override
    protected Toolbar getToolbarView() {
        return toolbar;
    }

    @Override
    protected boolean isShowBackArrow() {
        return true;
    }

    @Override
    protected int getToolbarMenu() {
        return 0;
    }

    @Override
    protected void setToolbarMenuClickEvent() {
        toolbar.setNavigationOnClickListener(view -> {
            hideInputKeyboard();
            finish();
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        search_progress.setVisibility(View.GONE);
        search_no_res.setVisibility(View.GONE);
        SearchView.SearchAutoComplete tv = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        tv.setTextColor(Color.WHITE);
        tv.setHintTextColor(Color.WHITE);
        searchView.onActionViewExpanded();
        initValues();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search_progress.setVisibility(View.VISIBLE);
                waterfallBean.data.clear();
                keyword = query;
                searchPresenter.loadSearchData(keyword, page);
                hideInputKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        search_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalCount = layoutManager.getItemCount();
                int[] lastPositions = new int[layoutManager.getSpanCount()];
                layoutManager.findLastCompletelyVisibleItemPositions(lastPositions);

                int lastPosition = max(lastPositions);
                if (!isLoading && totalCount < lastPosition + 2 && lastPosition != -1) {
                    ++page;
                    isLoading = true;
                    searchPresenter.loadSearchData(keyword, page);

                }
            }
        });
    }

    private void initValues() {
        waterfallBean = new WaterfallBean();
        waterfallBean.data = new ArrayList<>();
        tableAdapter = new WaterfallTableAdapter(waterfallBean, this,"unknown");
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        search_recyclerView.setLayoutManager(layoutManager);
        search_recyclerView.setAdapter(tableAdapter);
        searchPresenter = new SearchPresenterImpl(this);
    }

    private void hideInputKeyboard() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View v = SearchActivity.this.getCurrentFocus();
            if (v == null) {
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            searchView.clearFocus();
        }
    }

    @Override
    public void setSearchData(WaterfallBean waterfallBean) {
        this.waterfallBean.error_code = waterfallBean.error_code;
        this.waterfallBean.message = waterfallBean.message;
        this.waterfallBean.data.addAll(waterfallBean.data);
        tableAdapter.notifyDataSetChanged();
        if (waterfallBean.data.size() == 0 && page == 1) {
            search_no_res.setVisibility(View.VISIBLE);
        } else {
            search_no_res.setVisibility(View.GONE);
        }
        search_progress.setVisibility(View.GONE);
        isLoading = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
