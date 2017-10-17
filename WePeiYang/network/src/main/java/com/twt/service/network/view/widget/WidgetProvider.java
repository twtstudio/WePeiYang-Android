package com.twt.service.network.view.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.twt.service.network.R;
import com.twt.service.network.WifiStatusClass;
import com.twt.service.network.api.Api;
import com.twt.service.network.api.ApiPostClient;
import com.twt.service.network.view.NetActivity;
import com.twt.service.network.view.set.SetActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chen on 2017/8/26.
 */
//挥泪写完Widget
public class WidgetProvider extends AppWidgetProvider {
    private WifiStatusClass wifiStatusClass;
    private WidgetNetwork mWidgetNetwork;
    private static boolean status = false;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences mSharedPreferences2;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences.Editor mEditor2;
    private String username = null;
    private String password = null;
    private String switchCheck = null;

    //private static boolean status;
    //每次窗口小部件被更新时调用
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Intent intent = new Intent(context, SetActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Intent intent1 = new Intent(context, NetActivity.class);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);
        Intent intent2 = new Intent(Settings.ACTION_WIFI_SETTINGS);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
        //分别绑定id
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setOnClickPendingIntent(R.id.widget_net_refresh, getPendingIntent(context, R.id.widget_net_refresh));
        remoteViews.setOnClickPendingIntent(R.id.widget_net_set, getPendingIntent(context, R.id.widget_net_set));
        remoteViews.setOnClickPendingIntent(R.id.widget_net_set, pendingIntent);
        //remoteViews.setOnClickPendingIntent(R.id.widget_net_log, pendingIntent1);
        remoteViews.setOnClickPendingIntent(R.id.widget_net_wifi, pendingIntent2);
        remoteViews.setTextViewText(R.id.widget_net_wlan_status, "WLAN状态：" + getWifiStatus(context));
        remoteViews.setTextViewText(R.id.widget_net_wifi_name, "已连接校园网：" + getWifiName(context));
        remoteViews.setTextViewText(R.id.widget_net_wifi_status, "接入状态：" + getStatusName(context));
        remoteViews.setTextViewText(R.id.widget_net_log, setBtText(context));
        remoteViews.setOnClickPendingIntent(R.id.widget_net_log, getPendingIntent(context, R.id.widget_net_log));

        //更新widget
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    private PendingIntent getPendingIntent(Context context, int resID) {
        Intent intent = new Intent();
        intent.setClass(context, WidgetProvider.class);
        intent.setAction("com.twt.appwidget");
        intent.setData(Uri.parse("id:" + resID));
        //启动一个广播
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pendingIntent;
    }

    //接收窗口小部件点击时发送的广播
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("oooo", "1234");

        mSharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mSharedPreferences2 = context.getSharedPreferences("switch", Context.MODE_PRIVATE);
        mEditor2 = mSharedPreferences2.edit();
        mWidgetNetwork = new WidgetNetwork(context);
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        int resId = -1;
        if (intent.getAction().equals("com.twt.appwidget")) {
            Uri data = intent.getData();
            /**
             *getSchemeSpecificPart()方法
             * Gets the scheme-specific part of this URI, i.e.&nbsp;everything between
             * the scheme separator ':' and the fragment separator '#'. If this is a
             * relative URI, this method returns the entire URI. Decodes escaped octets.
             */
            if (data != null) {
                resId = Integer.parseInt(data.getSchemeSpecificPart());
            }
            if (resId == R.id.widget_net_refresh) {
                Toast.makeText(context, "已更新", Toast.LENGTH_SHORT).show();
                remoteViews.setTextViewText(R.id.widget_net_wlan_status, "WLAN状态：" + getWifiStatus(context));
                remoteViews.setTextViewText(R.id.widget_net_wifi_name, "已接入校园网：" + getWifiName(context));
                remoteViews.setTextViewText(R.id.widget_net_wifi_status, "接入状态：" + getStatusName(context));
                remoteViews.setTextViewText(R.id.widget_net_log, setBtText(context));

            } else {
            }

        }

        username = mSharedPreferences.getString("username", null);
        password = mSharedPreferences.getString("password", null);
        switchCheck = mSharedPreferences2.getString("checkswitch", null);

        if (resId == R.id.widget_net_log) {
            if (username != null && password != null) {
                if (setBtText(context) == "登录") {
                    if (getWifiStatus(context) == "关闭") {
                        Toast.makeText(context, "您尚未打开WLAN开关", Toast.LENGTH_SHORT).show();
                    }
                    mWidgetNetwork.loginPost(context, username, password);
                }
                if (setBtText(context) == "注销") {
                    mWidgetNetwork.logoutPost(context, username, password);
                    if (switchCheck == "checked") {
                        wifiStatusClass.closeWifi();
                    }
                }
            } else {
                Intent intent1 = new Intent(context, NetActivity.class);
                context.startActivity(intent1);
            }
            Log.d("cccc","START");
            remoteViews.setTextViewText(R.id.widget_net_wlan_status, "WLAN状态：" + getWifiStatus(context));
            remoteViews.setTextViewText(R.id.widget_net_wifi_name, "已接入校园网：" + getWifiName(context));
            remoteViews.setTextViewText(R.id.widget_net_wifi_status, "接入状态：" + getStatusName(context));
            remoteViews.setTextViewText(R.id.widget_net_log, setBtText(context));
        }

        //获得appwidget管理实例，用于管理appwidget以便进行更新操作
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        //相当于获得所有本程序创建的appwidget
        ComponentName componentName = new ComponentName(context, WidgetProvider.class);
        //更新widget
        manager.updateAppWidget(componentName, remoteViews);
    }

    private String getWifiName(Context context) {
        wifiStatusClass = new WifiStatusClass(context);
        String name = wifiStatusClass.getWifiInfo();
        switch (name) {
            case "\"" + "tjuwlan" + "\"":
                return "tjuwlan";
            case "\"" + "tju_lib" + "\"":
                return "tju_lib";
            default:
                return "无";
        }
    }

    private String getWifiStatus(Context context) {
        wifiStatusClass = new WifiStatusClass(context);
        int status = wifiStatusClass.checkStatus();
        Log.d("cccc", "ok");
        switch (status) {
            case 1:
                return "关闭";
            case 3:
                return "已打开";
            default:
                return null;
        }
    }

    private String setBtText(Context context) {
        wifiStatusClass = new WifiStatusClass(context);
        String name = wifiStatusClass.getWifiInfo();
        switch (name) {
            case "\"" + "tjuwlan" + "\"":
                Log.d("cccc", "wifiname:" + "true");
                if (textWlan(context)) {
                    return "注销";
                } else {
                    return "登录";
                }
            case "\"" + "tju_lib" + "\"":
                if (textWlan(context)) {
                    return "注销";
                } else {
                    return "登录";
                }
            default:
                return "登录";
        }
    }

    private String getStatusName(Context context) {
        if (textWlan(context)) {
            return "正常";
        } else {
            return "异常";
        }
    }

    public boolean textWlan(Context context) {
        Log.d("cccc","enter");
        Api api = ApiPostClient.getRetrofit2().create(Api.class);
        api.baidu()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Log.d("cccc","COM");
                    }

                    @Override
                    public void onError(Throwable e) {
                        //网络异常
                        Log.d("cccc", "e" + e);
                        status = false;

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = responseBody.string().trim();
                            Log.d("cccc", "15：" + string.substring(0, 15));
                            if (string.substring(0, 15).equals("<!DOCTYPE html>")) {
                                //网络正常 可以使用
                                status = true;
                                Log.d("cccc", "status[]:" + status);
                            } else {
                                //网络异常&连接tju但是没有登录&欠费
                                Log.d("cccc", "false");
                                status = false;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Log.d("cccc", "status22:" + status);
        return status;
    }
}
