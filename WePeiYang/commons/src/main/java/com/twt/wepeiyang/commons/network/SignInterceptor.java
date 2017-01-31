package com.twt.wepeiyang.commons.network;

import com.annimon.stream.Stream;
import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.JniUtils;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by retrox on 2017/1/25.
 */

public class SignInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();

        /**
         * deal with other requests (not twt requests)
         */
        if (!originRequest.url().host().equals("open.twtstudio.com")){
            return chain.proceed(originRequest);
        }

        HttpUrl newUrl = convert(originRequest.url());

        Request.Builder builder = originRequest.newBuilder()
//                    .addHeader("User-Agent", UserAgent.generate())
                .addHeader("Authorization", "Bearer{"+ CommonPrefUtil.getToken()+"}")
                .url(newUrl);

        Logger.d("token-->"+ CommonPrefUtil.getToken());

        return chain.proceed(builder.build());
    }

    private HttpUrl convert(HttpUrl originUrl) {

        String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis());

        List<String> keysList = new ArrayList<>(originUrl.queryParameterNames());
        keysList.add("t");
        Collections.sort(keysList);

        String keys = Stream.of(keysList).map(s1 -> {
            if ("t".equals(s1)){
                s1 += timestamp;
            }else {
                s1 += originUrl.queryParameter(s1);
            }
            return s1;
        }).reduce((value1, value2) -> value1 + value2).get();

        String sign = new String(Hex.encodeHex(DigestUtils.sha(JniUtils.getInstance().getAppKey() + keys + JniUtils.getInstance().getAppSecret()))).toUpperCase();

        return originUrl.newBuilder()
                .addQueryParameter("t", timestamp)
                .addQueryParameter("sign", sign)
                .addQueryParameter("app_key", JniUtils.getInstance().getAppKey())
                .build();
    }
}
