package com.twt.service.network.view.widget;

import android.content.Context;
import android.widget.Toast;

import com.twt.service.network.WifiStatusClass;
import com.twt.service.network.api.Api;
import com.twt.service.network.api.ApiPostClient;
import com.twt.service.network.modle.RequestParam;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cdc on 17-10-1.
 */

public class WidgetNetwork {
    private Context mContext;
    private WifiStatusClass mWifiStatusClass;
    private final static String action = "login";
    private final static String action1 = "logout";
    private static int ajax = 1;

    public WidgetNetwork(Context context) {
        this.mContext = context;
        mWifiStatusClass = new WifiStatusClass(context);
    }

    public void loginPost(Context context, String username, String password) {
        RequestParam requestParam = new RequestParam();
        requestParam.put("ac_id", "25");
        requestParam.put("nas_ip", "");
        requestParam.put("user_mac", "");
        requestParam.put("user_ip", "");
        requestParam.put("save_me", "1");
        requestParam.put("ajax", "1");
        Api api = ApiPostClient.getRetrofit().create(Api.class);
        api.loginPost(username, password, action, requestParam)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = responseBody.string().trim();
                            String text = string.substring(0, 3);
                            if (text.equals("INF")) {
                                Toast.makeText(context, "您尚未连接校园网", Toast.LENGTH_SHORT).show();
                            } else if (text.equals("log")) {
                                Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void logoutPost(Context context, String username, String password) {
        Api api = ApiPostClient.getRetrofit().create(Api.class);
        api.logoutPost(username, password, action1, ajax)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
