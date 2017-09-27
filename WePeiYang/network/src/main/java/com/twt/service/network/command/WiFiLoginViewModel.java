package com.twt.service.network.command;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twt.service.network.WifiStatusClass;
import com.twt.service.network.api.Api;
import com.twt.service.network.api.ApiPostClient;
import com.twt.service.network.modle.RequestParam;
import com.twt.service.network.R;
import com.twt.service.network.view.wifi.WiFiConnectFragment;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chen on 2017/7/10.
 */

public class WiFiLoginViewModel implements ViewModel {

    private Fragment mFragment;
    public boolean isRefresh = true;
    public static String NAME = null;
    public static String PASSWORD = null;
    private String action = "login";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;

    public final ObservableField<String> username = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableBoolean isChecked = new ObservableBoolean();

    public final ReplyCommand onLoginCommand = new ReplyCommand(() -> loginPost());

    public WiFiLoginViewModel(Fragment fragment) {
        this.mFragment = fragment;
        mSharedPreferences = fragment.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        initCheck();
    }

    public void resetData() {
        username.set(null);
        password.set(null);
    }

    public void loginPost() {
        if (isChecked.get()) {
            editor.putString("username", username.get());
            editor.putString("password", password.get());
            editor.putString("checkbox", "1");
            editor.commit();
        } else if (!isChecked.get()) {
            Log.d("cccc", "isChecked" + "false");
            editor.putString("username", null);
            editor.putString("password", null);
            editor.putString("checkbox", "2");
            editor.commit();
        }
        WifiStatusClass wifiStatusClass = new WifiStatusClass(mFragment.getContext());
        String wifiName = wifiStatusClass.getWifiInfo();
        WifiManager wifiManager = (WifiManager) mFragment.getContext().getSystemService(mFragment.getContext().WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            AlertDialog.Builder wifiCheck = new AlertDialog.Builder(mFragment.getContext())
                    .setTitle("您尚未连接校园网")
                    .setNegativeButton("我知道了", (dialog, which) -> dialog.dismiss());
            wifiCheck.create().show();
        }

        switch (wifiName) {
            case "\"" + "tjuwlan" + "\"":
                break;
            case "\"" + "tju_lib" + "\"":
                break;
            case "\"" + "\"":
                AlertDialog.Builder wifiCheck = new AlertDialog.Builder(mFragment.getContext())
                        .setTitle("您尚未连接校园网")
                        .setNegativeButton("我知道了", (dialog, which) -> dialog.dismiss());
                wifiCheck.create().show();
                break;
            default:
                break;
        }

        RequestParam requestParam = new RequestParam();
        requestParam.put("ac_id", "25");
        requestParam.put("nas_ip", "");
        requestParam.put("user_mac", "");
        requestParam.put("user_ip", "");
        requestParam.put("save_me", "1");
        requestParam.put("ajax", "1");
        Api api = ApiPostClient.getRetrofit().create(Api.class);
        api.loginPost(username.get(), password.get(), action, "25", requestParam, "1")
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = responseBody.string().trim();
                            String test = string.substring(0, 3);
                            if (test.equals("INF")) {
                                AlertDialog.Builder errorDialog = new AlertDialog.Builder(mFragment.getContext())
                                        .setTitle("您尚未连接校园网")
                                        .setNegativeButton("我知道了", (dialog, which) -> dialog.dismiss());
                                errorDialog.create().show();
                            } else if (test.equals("log")) {
                                NAME = username.get();
                                PASSWORD = password.get();
                                mFragment.getFragmentManager().beginTransaction()
                                        .replace(R.id.main_net_content, new WiFiConnectFragment(mFragment.getActivity()), null)
                                        .commit();
                            } else {
                                AlertDialog.Builder errorDialog = new AlertDialog.Builder(mFragment.getContext())
                                        .setTitle(string)
                                        .setMessage("请重新登录")
                                        .setNegativeButton("我知道了", (dialog, which) -> dialog.dismiss());
                                errorDialog.create().show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void initCheck() {
        Log.d("cccc", "isckeck" + mSharedPreferences.getString("checkbox", null));
        if (mSharedPreferences.getString("checkbox", null) != null) {
            switch (mSharedPreferences.getString("checkbox", null)) {
                case "1":
                    username.set(mSharedPreferences.getString("username", null));
                    password.set(mSharedPreferences.getString("password", null));
                    isChecked.set(true);
                    break;
                case "2":
                    isChecked.set(false);
                    break;
                default:
                    break;

            }
        }

    }
}
