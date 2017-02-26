package com.twtstudio.retrox.news.home;

import android.content.Context;
import android.databinding.ObservableArrayList;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.news.BR;
import com.twtstudio.retrox.news.R;
import com.twtstudio.retrox.news.api.HomeNewsBean;
import com.twtstudio.retrox.news.api.HomeNewsProvider;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

/**
 * Created by retrox on 26/02/2017.
 */

public class NewsListViewModel implements ViewModel {
    private Context mContext;

    public final ObservableArrayList<ViewModel> viewModels = new ObservableArrayList<>();

    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(NewsBannerViewModel.class, BR.viewModel, R.layout.item_home_news_banner)
            .put(NewsItemViewModel.class,BR.viewModel,R.layout.item_home_news)
            .build();

    public NewsListViewModel(Context mContext) {
        this.mContext = mContext;
        getData(false);
    }

    public void getData(boolean update){
        HomeNewsProvider provider = new HomeNewsProvider();
        provider.getHomeNews(update,this::processData);
    }

    private void processData(HomeNewsBean homeNewsBean){
        viewModels.add(new NewsBannerViewModel(homeNewsBean));
        for (HomeNewsBean.DataBean.NewsBean.AnnoucementsBean annoucementsBean : homeNewsBean.data.news.annoucements) {
            viewModels.add(new NewsItemViewModel(mContext,annoucementsBean));
        }
        for (HomeNewsBean.DataBean.NewsBean.JobsBean jobsBean : homeNewsBean.data.news.jobs) {
            viewModels.add(new NewsItemViewModel(mContext,jobsBean));
        }
    }
}
