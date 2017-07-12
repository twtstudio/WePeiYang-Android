package com.twt.service.home.user;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.cache.CacheProvider;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.auth.login.LoginActivity;
import com.twt.service.BuildConfig;
import com.twt.service.R;
import com.twt.service.settings.SettingsActivity;

/**
 * Created by retrox on 2017/1/14.
 */

public class PrefItemViewModel implements ViewModel {

    public static final int SETTINGS = 0;
    public static final int NIGHTMODE = 1;
    public static final int TJUOFFICAL = 2;
    public static final int EXIT = 3;


    private Context mContext;

    private int mMode = 0;

    public final ObservableBoolean preference = new ObservableBoolean(false);

    public final ObservableInt imageRes = new ObservableInt(0);

    public final ObservableField<String> title = new ObservableField<>();

    public ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean switchable = new ObservableBoolean(false);
        public final ObservableBoolean isMargin = new ObservableBoolean(false);
    }

    public ReplyCommand clickCommand = new ReplyCommand(this::onClick);

    public PrefItemViewModel(Context context, int mode) {
        mContext = context;
        mMode = mode;
        init();
    }


    private void init() {

        int mode = mMode;
        if (mode == NIGHTMODE) {
            imageRes.set(R.drawable.ic_nightmode);
            title.set("夜间模式");
            viewStyle.isMargin.set(true);
            viewStyle.switchable.set(true);
            preference.set(CommonPrefUtil.getThemeMode());
        } else if (mode == TJUOFFICAL) {
            // TODO: 2017/1/14 tju bind ?
        } else if (mode == SETTINGS) {
            // TODO: 2017/1/14 jump to settings
            imageRes.set(R.drawable.ic_settings);
            title.set("设置");
            viewStyle.isMargin.set(true);
            viewStyle.switchable.set(false);
        } else if (mode == EXIT) {
            imageRes.set(R.drawable.ic_settings_exit);
            viewStyle.isMargin.set(false);
            viewStyle.switchable.set(false);
            title.set("退出登录");
        }

        preference.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //ObservableBoolean observableBoolean = (ObservableBoolean) observable;
                if (mMode == NIGHTMODE) {
                    CommonPrefUtil.setThemeMode(preference.get());
                }
            }
        });

    }

    private void onClick() {
        Logger.d("pref click");
        if (mMode == SETTINGS) {
            //jump to settings
            Intent intent = new Intent(mContext, SettingsActivity.class);
            mContext.startActivity(intent);
//            ((AppCompatActivity)mContext).finish();
        } else if (mMode == EXIT) {
            //退出逻辑
            logout();
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setMessage("是否要登出？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //清除偏好数据和preference数据
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                        preferences.edit().clear().apply();
                        CommonPrefUtil.clearAll();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        //清除缓存
                        // TODO: 27/03/2017 最后清理下缓存 debug不清理
                        if (!BuildConfig.DEBUG) {
                            CacheProvider.clearCache();
                        }
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "真爱啊 TAT...", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }

}
