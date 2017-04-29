package com.twt.wepeiyang.commons.network;

import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by retrox on 29/04/2017.
 */

public class UaInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();

        /**
         * deal with other requests (not twt requests)
         */
        if (!originRequest.url().host().equals("open.twtstudio.com")){
            return chain.proceed(originRequest);
        }


        Request.Builder builder = originRequest.newBuilder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent",UserAgent.generate())
                .url(originRequest.url());

        Logger.d("token-->"+ CommonPrefUtil.getToken());

        return chain.proceed(builder.build());
    }
}
