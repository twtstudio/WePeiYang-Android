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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.twt.wepeiyang.commons.cache.CacheProvider;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.auth.login.LoginActivity;
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider;
import com.twtstudio.retrox.wepeiyangrd.BuildConfig;
import com.twtstudio.retrox.wepeiyangrd.R;
import com.twtstudio.retrox.wepeiyangrd.home.HomeActivity;

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
            exitTjuPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Logger.d(preference.toString());
                    Logger.d((boolean)newValue);
                    return true;
                }
            });

            Preference logOutPref = findPreference(getString(R.string.pref_log_out));
            logOutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
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
                                    if (!BuildConfig.DEBUG){
                                        CacheProvider.clearCache();
                                    }
                                    mContext.startActivity(intent);
                                    mContext.finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                    return true;
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
