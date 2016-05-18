package com.twt.service.api;

import android.text.TextUtils;

import com.twt.service.BuildConfig;
import com.twt.service.JniUtils;
import com.twt.service.model.Token;
import com.twt.service.support.PrefUtils;
import com.twt.service.support.UserAgent;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by huangyong on 16/5/18.
 */
public class WePeiYangClient {

    protected static final List<String> AUTH_MAP = new ArrayList<>();

    static {
        AUTH_MAP.add("auth/bind/tju");
        AUTH_MAP.add("auth/token/refresh");
        AUTH_MAP.add("gpa");
        AUTH_MAP.add("classtable");
        AUTH_MAP.add("news/comment/{id}");
    }

    protected Retrofit mRetrofit;

    protected Map<Object, CompositeSubscription> mSubscriptionsMap = new HashMap<>();

    private WePeiYang mService;

    private WePeiYangClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.LOG_ENABLE ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(sRequestInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://open.twtstudio.com/api/v1")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(WePeiYang.class);
    }

    private static class SingletonHolder {
        private static final WePeiYangClient INSTANCE = new WePeiYangClient();
    }

    public static WePeiYangClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    protected static Interceptor sRequestInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originRequest = chain.request();

            HttpUrl newUrl = convert(originRequest.url());

            Request.Builder builder = originRequest.newBuilder()
                    .addHeader("User-Agent", UserAgent.generate())
                    .url(newUrl);

            if (match(TextUtils.join("/", newUrl.pathSegments()))) {
                builder.addHeader("Authorization", PrefUtils.getToken());
            }

            return chain.proceed(builder.build());
        }

        protected HttpUrl convert(HttpUrl originUrl) {
            Set<String> keys = originUrl.queryParameterNames();
            keys.add("t");
            String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis());

            List<String> keysList = new ArrayList<>(keys);
            Collections.sort(keysList);

            StringBuilder builder = new StringBuilder();
            builder.append(JniUtils.getInstance().getAppKey());
            for (String key : keysList) {
                if ("t".equals(key)) {
                    builder.append(key).append(timestamp);
                } else {
                    builder.append(key).append(originUrl.queryParameter(key));
                }
            }
            builder.append(JniUtils.getInstance().getAppSecret());
            String sign = new String(Hex.encodeHex(DigestUtils.sha(builder.toString()))).toUpperCase();

            return originUrl.newBuilder()
                    .addQueryParameter("t", timestamp)
                    .addQueryParameter("sign", sign)
                    .build();
        }

        protected boolean match(String url) {
            // TODO: need test
            if (WePeiYangClient.AUTH_MAP.contains(url)) {
                return true;
            }

            for (String item : WePeiYangClient.AUTH_MAP) {
                String[] originSegments = item.split("/");
                String[] urlSegments = item.split("/");

                if (originSegments.length != urlSegments.length) {
                    continue;
                }

                boolean isMatch = true;
                for (int i = 0; i < originSegments.length; i++) {
                    if (originSegments[i].startsWith("{")) {
                        continue;
                    }
                    if (!originSegments[i].equalsIgnoreCase(urlSegments[i])) {
                        isMatch = false;
                        break;
                    }
                }

                if (isMatch) {
                    return isMatch;
                }
            }

            return false;
        }
    };

    public void unsubscribe(Object tag) {
        if (mSubscriptionsMap.containsKey(tag)) {
            CompositeSubscription subscriptions = mSubscriptionsMap.get(tag);
            subscriptions.unsubscribe();
            mSubscriptionsMap.remove(tag);
        }
    }

    protected void addSubscription(Object tag, Subscription subscription) {
        if (tag == null) {
            return;
        }
        CompositeSubscription subscriptions;
        if (mSubscriptionsMap.containsKey(tag)) {
            subscriptions = mSubscriptionsMap.get(tag);
        } else {
            subscriptions = new CompositeSubscription();
        }
        subscriptions.add(subscription);
        mSubscriptionsMap.put(tag, subscriptions);
    }

    public void login(Object tag, Subscriber<Token> subscriber, String username, String password) {
        Subscription subscription = mService.login(username, password)
                .map(new ResponseTransformer<Token>())
                .compose(APIUtils.<Token>applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

    public void bindTju(Object tag, Subscriber subscriber, String username, String password) {
        Subscription subscription = mService.bindTju(username, password)
                .map(new ResponseTransformer())
                .compose(APIUtils.applySchedulers())
                .subscribe(subscriber);
        addSubscription(tag, subscription);
    }

}
