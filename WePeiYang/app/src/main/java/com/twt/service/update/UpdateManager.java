package com.twt.service.update;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.twt.service.BuildConfig;
import com.twt.wepeiyang.commons.network.DefaultRetrofitBuilder;
import com.twt.wepeiyang.commons.network.RxErrorHandler;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 31/10/2017.
 */

public class UpdateManager {
    private UpdateApi api;

    private static final UpdateManager ourInstance = new UpdateManager();

    public static UpdateManager getInstance() {
        return ourInstance;
    }

    private UpdateManager() {
        api = DefaultRetrofitBuilder.getBuilder().baseUrl("https://mobile-api.twtstudio.com/").build().create(UpdateApi.class);
    }

    public void checkUpdate(Activity activity) {
        checkUpdate(activity,true);
    }

    public void checkUpdate(Activity activity,boolean toast) {

        api.checkUpdateInfo()
                .map(updateBean -> updateBean.info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(infoBean -> {
                    if (BuildConfig.FLAVOR.equals("dev")) {
                        if (Integer.parseInt(infoBean.beta.versionCode) > BuildConfig.VERSION_CODE) {
                            new AlertDialog.Builder(activity).setTitle("微北洋有Beta新版啦")
                                    .setMessage("更新日志: " + infoBean.beta.content + "\n更新时间: " + infoBean.beta.time + "\nVersion: " + infoBean.beta.version)
                                    .setPositiveButton("更新", (dialog, which) -> {
                                        Uri uri = Uri.parse(infoBean.beta.path);
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        activity.startActivity(intent);
                                    }).setNegativeButton("关闭", (dialog, which) -> {
                                dialog.dismiss();
                            })
                                    .show();
                        } else {
                            if (toast) {
                                Toast.makeText(activity,"已经是最新Beta版啦",Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (Integer.parseInt(infoBean.release.versionCode) > BuildConfig.VERSION_CODE) {
                            new AlertDialog.Builder(activity).setTitle("微北洋有新版啦")
                                    .setMessage("更新日志: " + infoBean.release.content + "\n更新时间: " + infoBean.release.time + "\nVersion: " + infoBean.release.version)
                                    .setPositiveButton("更新", (dialog, which) -> {
                                        Uri uri = Uri.parse(infoBean.release.path);
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        activity.startActivity(intent);
                                    }).setNegativeButton("关闭", (dialog, which) -> {
                                dialog.dismiss();
                            })
                                    .show();
                        } else {
                            if (toast){
                                Toast.makeText(activity,"已经是最新版啦",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, throwable -> {
                    new RxErrorHandler().call(throwable);
                });
    }

    public void setIsAutoCheck(boolean isAutoCheck) {
        Hawk.put("app_isAutoCheckUpdate",isAutoCheck);
    }

    public boolean getIsAutoCheck() {
        return Hawk.get("app_isAutoCheckUpdate",true);
    }
}
