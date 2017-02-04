package com.twtstudio.retrox.schedule;

import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictProvider;
import io.rx_cache.LifeCache;
import io.rx_cache.Reply;
import rx.Observable;

/**
 * Created by retrox on 2017/2/4.
 */

public interface ScheduleCacheApi {

    @LifeCache(duration = 30, timeUnit = TimeUnit.DAYS)
    Observable<Reply<ClassTable>> getClassTableAuto(Observable<ClassTable> obClassTable, DynamicKey dynamicKey, EvictProvider evictProvider);
}
