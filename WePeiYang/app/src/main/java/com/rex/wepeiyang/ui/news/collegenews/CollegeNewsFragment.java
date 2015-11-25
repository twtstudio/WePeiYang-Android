package com.rex.wepeiyang.ui.news.collegenews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.NewsItem;
import com.rex.wepeiyang.ui.BaseFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/19.
 */
public class CollegeNewsFragment extends BaseFragment implements CollegeNewsView{
    @InjectView(R.id.rv_collegenews)
    RecyclerView rvCollegenews;
    @InjectView(R.id.srl_collegenews)
    SwipeRefreshLayout srlCollegenews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collegenews, container, false);
        ButterKnife.inject(this, view);
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
    public void hideFooter() {

    }

    @Override
    public void showRefreshing() {

    }

    @Override
    public void hideRefreshing() {

    }

    @Override
    public void useFooter() {

    }

    @Override
    public void loadMoreItems(List<NewsItem> items) {

    }

    @Override
    public void refreshItems(List<NewsItem> items) {

    }
}
