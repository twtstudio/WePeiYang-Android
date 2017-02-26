package com.twtstudio.retrox.wepeiyangrd.home.news;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twt.wepeiyang.commons.view.RecyclerViewDivider;
import com.twtstudio.retrox.news.home.NewsListViewModel;
import com.twtstudio.retrox.wepeiyangrd.R;
import com.twtstudio.retrox.wepeiyangrd.base.BaseFragment;
import com.twtstudio.retrox.wepeiyangrd.databinding.FragmentNewsBinding;
import com.twtstudio.retrox.wepeiyangrd.databinding.FragmentNewsTwtBinding;

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
        boolean isChangeNewsSource = sharedPreferences.getBoolean(getString(R.string.pref_is_switch_news_source),true);
        View view = null;
        if (isChangeNewsSource){
            FragmentNewsBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_news,container,false);
            view = binding.getRoot();
            binding.setViewModel(new OneListViewModel());
            RecyclerView recyclerView = binding.recyclerView;

            RecyclerViewDivider divider = new RecyclerViewDivider.Builder(this.getContext())
                    .setSize(8f)
                    .setColorRes(R.color.background_gray)
                    .build();

            recyclerView.addItemDecoration(divider);
        }else {
            FragmentNewsTwtBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_news_twt,container,false);
            view = binding.getRoot();
            binding.setViewModel(new NewsListViewModel(this.getContext()));
        }

        return view;
    }
}
