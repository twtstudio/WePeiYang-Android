package com.twtstudio.retrox.wepeiyangrd.settings;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.twt.wepeiyang.commons.cache.CacheProvider;
import com.twt.wepeiyang.commons.network.RetrofitProvider;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.auth.login.AuthApi;
import com.twtstudio.retrox.auth.login.AuthSelfProvider;
import com.twtstudio.retrox.auth.login.LoginActivity;
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider;
import com.twtstudio.retrox.wepeiyangrd.BuildConfig;
import com.twtstudio.retrox.wepeiyangrd.R;
import com.twtstudio.retrox.wepeiyangrd.home.HomeActivity;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/2/21.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTitle("偏好设置");
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        CrashReport.testJavaCrash();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setmContext(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.settings_container, fragment)
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public static class SettingsFragment extends PreferenceFragment {

        private Activity mContext;

        public SettingsFragment() {
        }

        public void setmContext(Activity mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
            initPrefs();
        }

        public void initPrefs() {
            Preference exitTjuPref = findPreference(getString(R.string.pref_is_exit_tju));
            int dropOutMode = CommonPrefUtil.getDropOut();
            String dropOutSummary = "未操作";
            if (dropOutMode == 0){
                dropOutSummary = "未操作";
            }else if (dropOutMode == 1){
                dropOutSummary = "已退学";
            }else if (dropOutMode == 2){
                dropOutSummary = "已复学";
            }
            exitTjuPref.setSummary("退学状态: "+dropOutSummary);
            exitTjuPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String[] items = {"我要复学！","我要打游戏！","我要运动！","我要睡觉！","怎么样都好啦！"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                            .setTitle("选择你要办理的手续")
                            .setPositiveButton("我想好了...", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("我再想想吧...", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RetrofitProvider.getRetrofit().create(AuthApi.class)
                                            .dropOut(which)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(dropOutBean -> {
                                                if (!TextUtils.isEmpty(dropOutBean.data)){
                                                    Toast.makeText(mContext, dropOutBean.data, Toast.LENGTH_SHORT).show();

                                                    if (dropOutBean.data.equals("欢迎回来上学 (〃∀〃)")){
                                                        CommonPrefUtil.setDropOut(2);
                                                    }else if (dropOutBean.data.equals("退学成功 d(`･∀･)b")){
                                                        CommonPrefUtil.setDropOut(1);
                                                    }

                                                    //刷新下状态 --
                                                    int dropOutMode = CommonPrefUtil.getDropOut();
                                                    String dropOutSummary = "未操作";
                                                    if (dropOutMode == 0){
                                                        dropOutSummary = "未操作";
                                                    }else if (dropOutMode == 1){
                                                        dropOutSummary = "已退学";
                                                    }else if (dropOutMode == 2){
                                                        dropOutSummary = "已复学";
                                                    }
                                                    exitTjuPref.setSummary("退学状态: "+dropOutSummary);

                                                    //清空缓存 --- 课程表 GPA
                                                    CacheProvider.clearCache();

                                                    new AuthSelfProvider().getUserData(null); //null做了处理的 刷新状态
                                                }
                                                dialog.dismiss();
                                            },throwable -> {
                                                new AuthSelfProvider().getUserData(null); //null做了处理的
                                                new RxErrorHandler().call(throwable);
                                                dialog.dismiss();
                                            });
                                }
                            });

                    builder.create().show();
                    return false;
                }
            });

            Preference libBindPref = findPreference(getString(R.string.pref_bind_settings));
            libBindPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                            .setMessage("是否要跳转到绑定页面")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mContext,BindActivity.class);
                                    mContext.startActivity(intent);
                                }
                            });
                    builder.create().show();
                    return true;
                }
            });

            Preference isBindTju = findPreference(getString(R.string.pref_is_bind_tju));
            isBindTju.setSummary(CommonPrefUtil.getIsBindTju()?"已绑定":"未绑定");

            Preference isBindLib = findPreference(getString(R.string.pref_is_bind_lib));
            isBindLib.setSummary(CommonPrefUtil.getIsBindLibrary()?"已绑定":"未绑定");

            Preference isBindBike = findPreference(getString(R.string.pref_is_bind_bike));
            isBindBike.setSummary(CommonPrefUtil.getIsBindBike()?"已绑定":"未绑定");
        }

        private void processExitTju(){
            // TODO: 23/03/2017 退学逻辑
        }
    }


}
