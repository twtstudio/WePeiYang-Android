package com.twt.wepeiyang.commons.network;

import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by retrox on 2016/12/17.
 * @deprecated
 * moved to official interceptor for better stability
 */

public class LogInterceptor implements Interceptor {

    private final static String TAG = "HttpLog: ";
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        String header = request.header("Authorization");


        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        long tookms = TimeUnit.NANOSECONDS.toMillis(t2 - t1);
        if (header != null) {
            Log.d(TAG, "token--> "+header);
        }else {
            Log.d(TAG, "intercept: gggg");
        }

        Log.d(TAG, "time--> "+String.valueOf(tookms));
        Log.d(TAG, "url--> "+response.request().url().toString());
        Logger.json(response.body().string());
        return response;
    }
}
