package com.twt.service.ui.news.importantnews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.common.ui.PFragment;
import com.twt.service.interactor.ImportantNewsInteractorImpl;
import com.twt.service.model.NewsItem;
import com.twt.service.ui.common.OnRcvScrollListener;
import com.twt.service.ui.news.NewsAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ImportantNewsFragment extends PFragment<ImportantNewsPresenter> implements ImportantNewsViewController {

    @InjectView(R.id.rv_important_news)
    RecyclerView rvImportantNews;
    @InjectView(R.id.srl_important_news)
    SwipeRefreshLayout srlImportantNews;
    private NewsAdapter adapter;
    private ImportantNewsPresenter presenter;

    @Override
    protected ImportantNewsPresenter getPresenter() {
        if (mPresenter == null) {
            return new ImportantNewsPresenter(getContext());
        }
        return mPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_important_news;
    }

    @Override
    protected void initView() {
        srlImportantNews.setColorSchemeColors(getResources().getColor(R.color.news_primary_color));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new ImportantNewsAdapter(getActivity());
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
        if (message != null) {
            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
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
        if (srlImportantNews != null) {
            srlImportantNews.setRefreshing(refreshEnable);
        }
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
}
