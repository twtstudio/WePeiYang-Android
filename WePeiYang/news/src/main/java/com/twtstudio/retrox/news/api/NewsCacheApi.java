package com.twtstudio.retrox.news.api;

import com.twtstudio.retrox.news.api.bean.HomeNewsBean;

import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictProvider;
import io.rx_cache.LifeCache;
import rx.Observable;

/**
 * Created by retrox on 26/02/2017.
 */

public interface NewsCacheApi {

    @LifeCache(duration = 1,timeUnit = TimeUnit.DAYS)
    Observable<HomeNewsBean> getHomeNewsAuto(Observable<HomeNewsBean> newsBean, DynamicKey dynamicKey, EvictProvider evictProvider);
}
