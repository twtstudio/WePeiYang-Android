package com.twt.service.home.news;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.databinding.FragmentNewsTwtBinding;
import com.twt.wepeiyang.commons.view.RecyclerViewDivider;
import com.twtstudio.retrox.news.home.NewsListViewModel;
import com.twt.service.R;
import com.twt.service.base.BaseFragment;
import com.twt.service.databinding.FragmentNewsBinding;

/**
 * Created by retrox on 2016/12/12.
 */

public class NewsFragment extends BaseFragment {

    public static NewsFragment newInstance() {

        Bundle args = new Bundle();

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        boolean willChangeNewsSource = sharedPreferences.getBoolean(getString(R.string.pref_is_switch_news_source), false);
        View view = null;
        if (willChangeNewsSource) {
            FragmentNewsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
            view = binding.getRoot();
            binding.setViewModel(new OneListViewModel());
            RecyclerView recyclerView = binding.recyclerView;

            RecyclerViewDivider divider = new RecyclerViewDivider.Builder(this.getContext())
                    .setSize(8f)
                    .setColorRes(R.color.background_gray)
                    .build();

            recyclerView.addItemDecoration(divider);
        } else {
            FragmentNewsTwtBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_twt, container, false);
            view = binding.getRoot();
            binding.swipeRefreshNews.setColorSchemeResources(R.color.colorPrimary, R.color.assist_color_1, R.color.assist_color_2, R.color.schedule_purple2);
            binding.setViewModel(new NewsListViewModel(this.getContext()));
        }

        return view;
    }
}
