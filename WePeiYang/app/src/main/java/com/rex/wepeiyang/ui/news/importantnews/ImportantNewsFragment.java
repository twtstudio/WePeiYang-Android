package com.rex.wepeiyang.ui.news.importantnews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.NewsItem;
import com.rex.wepeiyang.interactor.ImportantNewsInteractorImpl;
import com.rex.wepeiyang.ui.common.OnRcvScrollListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ImportantNewsFragment extends Fragment implements ImportantNewsView {

    @InjectView(R.id.rv_important_news)
    RecyclerView rvImportantNews;
    @InjectView(R.id.srl_important_news)
    SwipeRefreshLayout srlImportantNews;
    private ImportantNewsAdapter adapter;
    private ImportantNewsPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new ImportantNewsAdapter(getActivity());
        View view = inflater.inflate(R.layout.fragment_important_news, container, false);
        ButterKnife.inject(this, view);
        srlImportantNews.setColorSchemeColors(getResources().getColor(R.color.news_primary_color));
        presenter = new ImportantNewsPresenterImpl(this, new ImportantNewsInteractorImpl());
        rvImportantNews.setOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                presenter.loadMoreNewsItems();
                super.onBottom();
            }
        });
        rvImportantNews.setAdapter(adapter);
        rvImportantNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        presenter.refreshNewsItems();
        srlImportantNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshNewsItems();
            }
        });
        return view;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void showRefreshing() {
        if (srlImportantNews.isRefreshing()) {
            return;
        } else {
            srlImportantNews.setRefreshing(true);
        }

    }

    @Override
    public void hideRefreshing() {
        srlImportantNews.setRefreshing(false);
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
