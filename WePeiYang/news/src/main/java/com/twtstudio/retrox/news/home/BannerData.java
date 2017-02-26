package com.twtstudio.retrox.news.home;

import com.twtstudio.retrox.news.api.HomeNewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by retrox on 26/02/2017.
 */

public class BannerData {
    private List<String> urls = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<Integer> indexList = new ArrayList<>();


    public BannerData(HomeNewsBean homeNewsBean){
        List<HomeNewsBean.DataBean.CarouselBean> carouselBeanList = homeNewsBean.data.carousel;
        for (HomeNewsBean.DataBean.CarouselBean carouselBean : carouselBeanList) {
            urls.add(carouselBean.pic);
            titles.add(carouselBean.subject);
            indexList.add(carouselBean.index);
        }
    }

    public List<String> getUrls() {
        return urls;
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<Integer> getIndexList() {
        return indexList;
    }
}
