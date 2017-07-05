package com.twtstudio.retrox.gpa;


import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictProvider;
import io.rx_cache.LifeCache;
import io.rx_cache.Reply;
import rx.Observable;

/**
 * Created by retrox on 2017/2/3.
 */

public interface GpaCacheProvider {

    @LifeCache(duration = 180,timeUnit = TimeUnit.DAYS)
    Observable<Reply<MyGpaBean>> getGpaAuto(Observable<MyGpaBean> obGpa, DynamicKey value, EvictProvider evictProvider);
}
