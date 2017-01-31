package com.twtstudio.retrox.wepeiyangrd.cache;

import rx.Observable;

/**
 * Created by retrox on 2016/12/10.
 */

public interface ICache {
    <T> Observable<T> get(String key,Class<T> cls);

    <T> void put(String key,T t);
}
