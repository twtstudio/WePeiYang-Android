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
import android.view.View;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.cache.CacheProvider;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.auth.login.LoginActivity;
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider;
import com.twtstudio.retrox.wepeiyangrd.R;

/**
 * Created by retrox on 2017/2/21.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTitle("偏好设置");

        SettingsFragment fragment = new SettingsFragment();

        getFragmentManager().beginTransaction()
                .replace(R.id.settings_container, fragment)
                .commit();

    }

    public static class SettingsFragment extends PreferenceFragment {

        private Activity mContext;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
            mContext = this.getActivity();
            initPrefs();
        }

        public void initPrefs() {
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
                                    CacheProvider.clearCache();
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

            Preference libBindPref = findPreference(getString(R.string.pref_is_bind_lib));
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
        }
    }
}
