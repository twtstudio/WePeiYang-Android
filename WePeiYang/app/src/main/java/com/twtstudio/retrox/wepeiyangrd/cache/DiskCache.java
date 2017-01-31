package com.twtstudio.retrox.wepeiyangrd.cache;


import com.orhanobut.logger.Logger;
import com.twtstudio.retrox.wepeiyangrd.WePeiYangApp;
import com.twtstudio.retrox.wepeiyangrd.support.NetworkUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2016/12/10.
 * for DiskCache function
 */

public class DiskCache implements ICache {
    private static final String NAME = ".cache";

    public static long OTHER_CACHE_TIME = 10 * 60 * 1000;
    public static long WIFI_CACHE_TIME = 30 * 60 * 1000;

    public static final int GPA = 1;
    public static final int Secdule = 2;

    private static List<String> longTimeDataKey = new ArrayList<>();

    /**
     * some need long time save
     */
    static {
        longTimeDataKey.add("key1");
        longTimeDataKey.add("key2");
    }


    private File fileDir;

    public DiskCache() {
        fileDir = WePeiYangApp.getContext().getCacheDir();
    }


    @Override
    public <T> Observable<T> get(String key, Class<T> cls) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                T t = (T) getDiskData(key + NAME);

                if (subscriber.isUnsubscribed()) {
                    return;
                }

                if (t == null) {
                    subscriber.onNext(null);
                } else {
                    subscriber.onNext(t);
                }

                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public <T> void put(String key, T t) {
        Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                boolean isSuccess = saveCache(key + NAME, t);

                if (!subscriber.isUnsubscribed() && isSuccess) {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        ;
    }

    private Object getDiskData(String fileName) {
        File file = new File(fileDir, fileName);

        if (isCacheDataExpired(file)) {
            return null;
        }

        if (!file.exists()) {
            return null;
        }

        Object o = null;
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(file));
            o = inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e("read error", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Logger.e("error class not found", e);
        } finally {
            closeStream(inputStream);
        }
        return o;
    }

    private boolean isCacheDataExpired(File dataFile) {
        if (!dataFile.exists()) {
            return false;
        }
        long existTime = System.currentTimeMillis() - dataFile.lastModified();
        boolean isFailure = false;
        if (isLongTimeData(getKey(dataFile))){
            isFailure = false;
        }else {
            if (NetworkUtils.getNetworkType(WePeiYangApp.getContext()) == NetworkUtils.NetworkType.NETWORK_WIFI) {
                isFailure = existTime > WIFI_CACHE_TIME;
            } else {
                isFailure = existTime > OTHER_CACHE_TIME;
            }
        }

        return isFailure;
    }

    private <T> boolean saveCache(String fileName, T t) {
        File file = new File(fileDir, fileName);


        ObjectOutputStream objectOutputStream = null;
        boolean isSuccess = false;

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOut);
            objectOutputStream.writeObject(t);
            objectOutputStream.flush();
            isSuccess = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(objectOutputStream);
            closeStream(fileOut);
        }

        return isSuccess;

    }

    private void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.e("stream close exception", e);
            }
        }
    }

    public void clearDiskCache(String key){
        File file = new File(fileDir,key + NAME);
        if (file.exists()){
            file.delete();
        }
    }

    private String getKey(File file){
        String name = file.getName();
        String[] strings = name.split("\\.");
        return strings[0];
    }

    private boolean isLongTimeData(String key){
        return longTimeDataKey.contains(key);
    }
}
