package com.twt.service.network.command;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.network.R;
import com.twt.service.network.WifiStatusClass;
import com.twt.service.network.api.Api;
import com.twt.service.network.api.ApiPostClient;
import com.twt.service.network.view.wifi.WifiFragment;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by cdc on 17-10-28.
 */

public class WifiLogoutCommand {
    private WifiStatusClass mWifiStatusCLass;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Fragment mFragment;
    private TextView mWifiName;
    private TextView mStudentID;
    private TextView mIPAddress;
    private String action_logout = "logout";
    private static int ajax = 1;

    public WifiLogoutCommand(Fragment fragment, TextView wifiName, TextView studentID, TextView ipAddress) {
        this.mFragment = fragment;
        this.mWifiName = wifiName;
        this.mStudentID = studentID;
        this.mIPAddress = ipAddress;
        this.mWifiStatusCLass = new WifiStatusClass(mFragment.getActivity());
        mSharedPreferences = mFragment.getContext().getSharedPreferences("autoDis", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void setText() {
        String wifiname = mWifiStatusCLass.getWifiInfo();
        String wifiIP = mWifiStatusCLass.getIPAddress();
        mWifiName.setText("热点名称：" + wifiname);
        mStudentID.setText("学号：" + WifiLoginCommand.NAME);
        mIPAddress.setText("IP地址："+wifiIP);
    }

    public void onLogout() {
        Api api = ApiPostClient.getRetrofit().create(Api.class);
        api.logoutPost(WifiLoginCommand.NAME, WifiLoginCommand.PASSWORD, action_logout, ajax)
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
                            Toast.makeText(mFragment.getContext(), responseBody.string().trim(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mFragment.getFragmentManager().beginTransaction()
                                .replace(R.id.main_net_content, new WifiFragment(), null)
                                .commit();
                        initDis();
                    }
                });
    }

    public void onSelfService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mFragment.getContext())
                .setTitle("是否要打开浏览器？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri uri = Uri.parse("http://202.113.4.11:8800/");
                        intent.setData(uri);
                        mFragment.getContext().startActivity(intent);
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void initDis() {
        if (mSharedPreferences.getString("disConnection", null) != null) {
            Log.d("ffff", "receive" + mSharedPreferences.getString("disConnection", null));
            if (mSharedPreferences.getString("disConnection", null) == "1") {
                mWifiStatusCLass.closeWifi();
            }
        }
    }
}
