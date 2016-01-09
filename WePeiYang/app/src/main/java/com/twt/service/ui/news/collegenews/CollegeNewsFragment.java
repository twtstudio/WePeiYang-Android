package com.twt.service.ui.news.collegenews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.bean.NewsItem;
import com.twt.service.interactor.CollegeNewsInteractorImpl;
import com.twt.service.ui.BaseFragment;
import com.twt.service.ui.common.OnRcvScrollListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/19.
 */
public class CollegeNewsFragment extends BaseFragment implements CollegeNewsView {
    @InjectView(R.id.rv_collegenews)
    RecyclerView rvCollegenews;
    @InjectView(R.id.srl_collegenews)
    SwipeRefreshLayout srlCollegenews;
    private CollegeNewsAdapter adapter;
    private CollegeNewsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collegenews, container, false);
        ButterKnife.inject(this, view);
        adapter = new CollegeNewsAdapter(getActivity());
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        rvCollegenews.setLayoutManager(manager);
        rvCollegenews.setAdapter(adapter);
        presenter = new CollegeNewsPresenterImpl(this, new CollegeNewsInteractorImpl());
        presenter.refreshItems();
        srlCollegenews.setColorSchemeColors(getResources().getColor(R.color.news_primary_color));
        srlCollegenews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshItems();
            }
        });
        rvCollegenews.setOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                super.onBottom();
                presenter.loadMoreItems();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void loadMoreItems(List<NewsItem> items) {
        adapter.addItems(items);
    }

    @Override
    public void refreshItems(List<NewsItem> items) {
        adapter.refreshItems(items);
    }

    @Override
    public void setRefreshEnable(boolean refreshEnable) {
        srlCollegenews.setRefreshing(refreshEnable);
    }
}
