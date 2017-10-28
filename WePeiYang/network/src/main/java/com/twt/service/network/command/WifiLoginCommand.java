package com.twt.service.network.command;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.twt.service.network.R;
import com.twt.service.network.api.Api;
import com.twt.service.network.api.ApiPostClient;
import com.twt.service.network.modle.RequestParam;
import com.twt.service.network.view.wifi.WiFiConnectFragment;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cdc on 17-10-28.
 */

public class WifiLoginCommand {
    private Fragment mFragment;
    private EditText mUsername;
    private EditText mPassword;
    private String username;
    private String password;
    public static String NAME = null;
    public static String PASSWORD = null;
    private String action_login="login";
    private String action_logout = "logout";
    private static int ajax = 1;

    public WifiLoginCommand(Fragment fragment, EditText username_et, EditText password_et){
        this.mFragment = fragment;
        this.mUsername = username_et;
        this.mPassword = password_et;
        this.username = String.valueOf(username_et.getText());
        this.password = String.valueOf(password_et.getText());
    }
    public void resetData(){
        mUsername.setText(null);
        mPassword.setText(null);
    }

    public void login(){
        RequestParam requestParam = new RequestParam();
        requestParam.put("ac_id", "25");
        requestParam.put("nas_ip", "");
        requestParam.put("user_mac", "");
        requestParam.put("user_ip", "");
        requestParam.put("save_me", "1");
        requestParam.put("ajax", "1");
        Api api = ApiPostClient.getRetrofit().create(Api.class);
        api.loginPost(username, password, action_login, requestParam)
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
                            String test = string.substring(0, 3);
                            if (test.equals("INF")) {
                                AlertDialog.Builder errorDialog = new AlertDialog.Builder(mFragment.getContext())
                                        .setTitle("您尚未连接校园网")
                                        .setNegativeButton("我知道了", (dialog, which) -> dialog.dismiss());
                                errorDialog.create().show();
                            } else if (test.equals("log")) {
                                NAME = username;
                                PASSWORD = password;
                                mFragment.getFragmentManager().beginTransaction()
                                        .replace(R.id.main_net_content, new WiFiConnectFragment(mFragment.getActivity()), null)
                                        .commit();
                            } else {
                                AlertDialog.Builder errorDialog = new AlertDialog.Builder(mFragment.getContext())
                                        .setTitle(string)
                                        .setNegativeButton("我知道了", (dialog, which) -> dialog.dismiss());
                                errorDialog.create().show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    public void logout(){
        Api api = ApiPostClient.getRetrofit().create(Api.class);
        api.logoutPost(username, password, action_logout, ajax)
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
}
