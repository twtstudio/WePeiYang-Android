package com.twt.service.network.command;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twt.service.network.WifiStatusClass;
import com.twt.service.network.api.Api;
import com.twt.service.network.api.ApiPostClient;
import com.twt.service.network.modle.LoginBean;
import com.twt.service.network.R;
import com.twt.service.network.view.wifi.WifiFragment;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static com.twt.service.network.view.set.SetActivity.SetFragment.dis;

/**
 * Created by chen on 2017/7/12.
 */

public class WiFiConnectViewModel implements ViewModel {
    private Fragment mFragment;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private WifiStatusClass wifiInfo;

    public static int ajax = 1;
    public String action = "logout";

    public LoginBean logoutBean;

    //public final ObservableField<String> wlan = new ObservableField<>();
    public String wlan;
    public String username;
    public String studentId;
    public String onlineStatus;
    private String passWord;

    public WiFiConnectViewModel(Fragment fragment) {
        this.mFragment = fragment;
        init();
    }

    public WiFiConnectViewModel(Context context) {
        this.mContext = context;
    }

    public final ReplyCommand onLogoutCommand = new ReplyCommand(() -> logoutTry());

    public void init() {
        wifiInfo = new WifiStatusClass(mFragment.getActivity());
        //wlan.set(wifiInfo.getWifiInfo());
        wlan = wifiInfo.getWifiInfo();
        mSharedPreferences = mFragment.getContext().getSharedPreferences("autoDis", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
//        mEditor.putString("disConnection",dis);
//        mEditor.commit();
        studentId = WiFiLoginViewModel.NAME;
        passWord = WiFiLoginViewModel.PASSWORD;
    }

    public void logoutTry() {
        mEditor.putString("disConnection", dis);
        mEditor.commit();
        Api api = ApiPostClient.getRetrofit().create(Api.class);
        api.logoutPost(studentId, passWord, action, ajax)
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
                        Toast.makeText(mFragment.getContext(), "网络已断开", Toast.LENGTH_SHORT).show();
                        mFragment.getFragmentManager().beginTransaction()
                                .replace(R.id.main_net_content, new WifiFragment(), null)
                                .commit();
                        initDis();
                    }
                });


    }

    //    public void logoutWidget() {
//        Api api = ApiPostClient.getRetrofit().create(Api.class);
//        api.logoutPost(studentId.get(), passWord.get(), action, ajax)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ResponseBody>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d("wwww", "3455");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d("wwww", "2345" + e);
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody responseBody) {
//                        Log.d("wwww", String.valueOf(responseBody));
//                        Toast.makeText(mContext, "网络已断开", Toast.LENGTH_SHORT).show();
//                        mFragment.getFragmentManager().beginTransaction()
//                                .replace(R.id.main_net_content, new WifiFragment(), null)
//                                .commit();
//                    }
//
//                });
//    }
    private void initDis() {
        if (mSharedPreferences.getString("disConnection", null) != null) {
            Log.d("ffff", "receive" + mSharedPreferences.getString("disConnection", null));
            if (mSharedPreferences.getString("disConnection", null) == "1") {
                wifiInfo.closeWifi();
            }
        }
    }

}
