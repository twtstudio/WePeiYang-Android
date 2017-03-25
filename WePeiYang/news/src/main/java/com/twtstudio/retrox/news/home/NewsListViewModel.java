package com.twtstudio.retrox.news.home;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twtstudio.retrox.news.BR;
import com.twtstudio.retrox.news.R;
import com.twtstudio.retrox.news.api.CommonNewsBean;
import com.twtstudio.retrox.news.api.HomeNewsBean;
import com.twtstudio.retrox.news.api.HomeNewsProvider;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

/**
 * Created by retrox on 26/02/2017.
 */

public class NewsListViewModel implements ViewModel {
    private Context mContext;

    public final ObservableBoolean isRefreshing = new ObservableBoolean(false);

    public final ReplyCommand refreshCommand = new ReplyCommand(() -> getData(true));

    public final ObservableArrayList<ViewModel> viewModels = new ObservableArrayList<>();

    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(NewsBannerViewModel.class, BR.viewModel, R.layout.item_home_news_banner)
            .put(NewsItemViewModel.class, BR.viewModel, R.layout.item_home_news)
            .build();

    public NewsListViewModel(Context mContext) {
        this.mContext = mContext;
        getData(false);
    }

    public void getData(boolean update) {
        if (update) {
            isRefreshing.set(true);
        }
        HomeNewsProvider provider = new HomeNewsProvider();
        provider.getHomeNews(update, this::processData);
        provider.getCommonNews(this::processNewsData);
    }

    private void processNewsData(CommonNewsBean.DataBean dataBean) {
        viewModels.add(new NewsItemViewModel(mContext, dataBean));
    }

    private void processData(HomeNewsBean homeNewsBean) {
        isRefreshing.set(false);
        viewModels.clear();
        viewModels.add(new NewsBannerViewModel(homeNewsBean));
        for (HomeNewsBean.DataBean.NewsBean.AnnoucementsBean annoucementsBean : homeNewsBean.data.news.annoucements) {
            viewModels.add(new NewsItemViewModel(mContext, annoucementsBean));
        }
        // TODO: 26/03/2017 就业模块
//        for (HomeNewsBean.DataBean.NewsBean.JobsBean jobsBean : homeNewsBean.data.news.jobs) {
//            viewModels.add(new NewsItemViewModel(mContext, jobsBean));
//        }
    }
}
