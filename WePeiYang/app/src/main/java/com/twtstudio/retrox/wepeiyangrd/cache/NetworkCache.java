package com.twtstudio.retrox.wepeiyangrd.cache;

import rx.Observable;

/**
 * Created by retrox on 2016/12/10.
 */

public abstract class NetworkCache<T> {
    public abstract Observable<T> get(String key,Class<T> cls);
}
