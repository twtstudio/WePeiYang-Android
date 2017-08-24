package com.twtstudio.service.classroom.model;

import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictProvider;
import io.rx_cache.LifeCache;
import io.rx_cache.Reply;
import rx.Observable;

/**
 * Created by zhangyulong on 7/10/17.
 */

public interface ClassRoomCacheApi {
    @LifeCache(duration = 1, timeUnit = TimeUnit.SECONDS)
    Observable<Reply<FreeRoom2>> getFreeRoomAuto(Observable<FreeRoom2> obClassTable, DynamicKey dynamicKey, EvictProvider evictProvider);
    @LifeCache(duration = 1, timeUnit = TimeUnit.SECONDS)
    Observable<Reply<CollectedRoom2>> getAllCollectedClassroomAuto(Observable<CollectedRoom2> obClassTable, DynamicKey dynamicKey, EvictProvider evictProvider);
    @LifeCache(duration = 1, timeUnit = TimeUnit.SECONDS)
    Observable<Reply<ClassRoomApiReaponse<collectApiResponse>>> collectAuto(Observable<ClassRoomApiReaponse<collectApiResponse>> obClassTable, DynamicKey dynamicKey, EvictProvider evictProvider);
    @LifeCache(duration = 1, timeUnit = TimeUnit.SECONDS)
    Observable<Reply<ClassRoomApiReaponse>> cancelCollectAuto(Observable<ClassRoomApiReaponse> obClassTable, DynamicKey dynamicKey, EvictProvider evictProvider);

}
