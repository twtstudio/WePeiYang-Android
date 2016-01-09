package com.twt.service.ui.news.viewpoint;

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
import com.twt.service.interactor.ViewPointInteractorImpl;
import com.twt.service.ui.BaseFragment;
import com.twt.service.ui.common.OnRcvScrollListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/18.
 */
public class ViewPointFragment extends BaseFragment implements ViewPointView {
    @InjectView(R.id.rv_viewpoint)
    RecyclerView rvViewpoint;
    @InjectView(R.id.srl_viewpoint)
    SwipeRefreshLayout srlViewpoint;
    private ViewPointAdapter adapter;
    private ViewPointPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_viewpoint, container, false);
        ButterKnife.inject(this, view);
        presenter = new ViewPointPresenterImpl(this, new ViewPointInteractorImpl());
        adapter = new ViewPointAdapter(getActivity());
        rvViewpoint.setAdapter(adapter);
        rvViewpoint.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvViewpoint.setOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                super.onBottom();
                presenter.loadMoreItems();
            }
        });
        presenter.refreshItems();
        srlViewpoint.setColorSchemeColors(getResources().getColor(R.color.news_primary_color));
        srlViewpoint.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshItems();
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
    public void toastMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshItems(List<NewsItem> items) {
        adapter.refreshItems(items);
    }

    @Override
    public void loadMoreItems(List<NewsItem> items) {
        adapter.addItems(items);
    }

    @Override
    public void setRefreshEnable(boolean refreshEnable) {
        srlViewpoint.setRefreshing(false);
    }
}
