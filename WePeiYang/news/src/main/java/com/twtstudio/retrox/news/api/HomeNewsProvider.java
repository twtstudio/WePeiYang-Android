package com.twtstudio.retrox.news.api;

import com.twt.wepeiyang.commons.cache.CacheProvider;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twtstudio.retrox.news.api.bean.CommonNewsBean;
import com.twtstudio.retrox.news.api.bean.HomeNewsBean;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 26/02/2017.
 */

public class HomeNewsProvider {
    private NewsApi newsApi;
    private NewsCacheApi newsCacheApi;


    public HomeNewsProvider() {
        newsApi = RetrofitProvider.getRetrofit().create(NewsApi.class);
        newsCacheApi = CacheProvider.getRxCache().using(NewsCacheApi.class);
    }

    public void getHomeNews(boolean update, Action1<HomeNewsBean> action1){
        newsCacheApi.getHomeNewsAuto(newsApi.getHomeNews(),new DynamicKey("homeNews"),new EvictDynamicKey(update))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1,throwable -> new RxErrorHandler().call(throwable.getCause()));
    }

    public void getCommonNews(Action1<CommonNewsBean.DataBean> action1){

        Observable.merge(newsApi.getNews(1,1),newsApi.getNews(1,2),newsApi.getNews(3,1))
                .map(commonNewsBean -> commonNewsBean.data)
                .flatMap(Observable::from)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1,new RxErrorHandler());

    }
}
