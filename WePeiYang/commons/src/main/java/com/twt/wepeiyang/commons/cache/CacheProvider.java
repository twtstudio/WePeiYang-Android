package com.twt.wepeiyang.commons.cache;

import com.twt.wepeiyang.commons.utils.App;

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
                .persistence(App.getApplicationContext().getCacheDir(),new GsonSpeaker());
    }

    private static class SingletonHolder {
        private static final CacheProvider INSTANCE = new CacheProvider();
    }

    public static RxCache getRxCache(){
        return SingletonHolder.INSTANCE.mRxCache;
    }

    public static void clearCache(){
        File[] files =  App.getApplication().getFilesDir().listFiles();
        for (File file: files) {
            file.delete();
        }
    }
}
