package com.twtstudio.retrox.wepeiyangrd.cache;


import com.orhanobut.logger.Logger;

import rx.Observable;

/**
 * Created by retrox on 2016/12/10.
 */

public class CacheLoader {
    private ICache mMemoryCache, mDiskCache;

    public CacheLoader() {
        mDiskCache = new DiskCache();
        mMemoryCache = new MemoryCache();
    }

    private static CacheLoader sCacheLoader;

    public static CacheLoader getInstance() {
        if (sCacheLoader == null) {
            synchronized (CacheLoader.class) {
                if (sCacheLoader == null) {
                    sCacheLoader = new CacheLoader();
                }
            }
        }
        return sCacheLoader;
    }

    public <T> Observable<T> asDataObeservable(String key, Class<T> cls, NetworkCache<T> netwrokCache) {
        Observable observable =
                Observable.concat(memory(key, cls),
                        disk(key, cls),
                        network(key, cls, netwrokCache))
                        .first(t -> t != null);
        return observable;
    }

    private <T> Observable<T> memory(String key, Class<T> cls) {
        return mMemoryCache.get(key, cls)
                .doOnNext(t -> {
                    if (null != t) {
                        Logger.d("from memory cache", t);
                    }
                });
    }

    private <T> Observable<T> disk(String key, Class<T> cls) {
        return mDiskCache.get(key, cls)
                .doOnNext(t -> {
                    if (null != t) {
                        Logger.d("from disk cache", t);
                        mMemoryCache.put(key, cls);
                    }
                });
    }

    private <T> Observable<T> network(String key, Class<T> cls, NetworkCache<T> networkCache) {
        return networkCache.get(key, cls)
                .doOnNext(t -> {
                    if (null != t) {
                        Logger.d("load from network", t);
                        mMemoryCache.put(key, t);
                        mDiskCache.put(key, t);
                    }
                });
    }

    public void clearMemory(String key) {
        ((MemoryCache) mMemoryCache).clearMemory(key);
    }

    public void clearMemoryDisk(String key) {
        clearMemory(key);
        ((DiskCache) mDiskCache).clearDiskCache(key);
    }

}
