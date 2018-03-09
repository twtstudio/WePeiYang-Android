package com.twt.wepeiyang.commons.cache;

import com.twt.wepeiyang.commons.experimental.Commons;

import java.io.File;

import io.rx_cache.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by retrox on 2017/2/4.
 */

public class CacheProvider {

    private RxCache mRxCache;

    private CacheProvider() {
        mRxCache = new RxCache.Builder()
                .persistence(Commons.INSTANCE.getApplicationContext().getCacheDir(), new GsonSpeaker());
    }

    public static RxCache getRxCache() {
        return SingletonHolder.INSTANCE.mRxCache;
    }

    public static void clearCache() {
        File[] files = Commons.INSTANCE.getApplicationContext().getCacheDir().listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    private static class SingletonHolder {
        private static final CacheProvider INSTANCE = new CacheProvider();
    }
}
