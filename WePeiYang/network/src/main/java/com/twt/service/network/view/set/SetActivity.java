package com.twt.service.network.view.set;

import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.kelin.mvvmlight.messenger.Messenger;
import com.twt.service.network.R;
import com.twt.service.network.R2;
import com.twt.service.network.view.NetActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chen on 2017/7/15.
 */

public class SetActivity extends AppCompatActivity{
    @BindView(R2.id.net_set_toolbar)
    Toolbar mToolbar;
    private Unbinder mUnbinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_set);
        mUnbinder= ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitle("设置");
        SetFragment setFragment=new SetFragment(this);
        setFragment.setContext(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.set_container,setFragment)
                .commit();
    }

    public static class SetFragment extends PreferenceFragment{
        private Activity mContext;
        public int index;
        public static String dis="0";
        public CharSequence[] entries;

        public void setContext(Activity activity){
            this.mContext=activity;
        }
        public SetFragment(Activity mContext){
            this.mContext=mContext;
        }
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.network_settings_pref);
            initPref();
        }
        public void initPref(){
            ListPreference default_order= (ListPreference) findPreference(getString(R.string.pref_default_connect_order));
            Log.d("ffff","lp:"+default_order.getValue());
            SwitchPreference switch1= (SwitchPreference) findPreference(getString(R.string.pref_auto_connect_wlan));
            switch1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (switch1.isChecked()){
                        switch (default_order.getValue()){
                            case "0":
                                String autoConnection="1";
                                Messenger.getDefault().send(autoConnection,NetActivity.TOKEN1);
                                break;
                            case "1":
                                String autoConnection1="2";
                                Messenger.getDefault().send(autoConnection1,NetActivity.TOKEN1);
                                break;
                            default:
                                break;
                        }
                    }
                    if (!switch1.isChecked()){
                        String autoConnection="3";
                        Messenger.getDefault().send(autoConnection,NetActivity.TOKEN1);
                    }
                    return true;
                }
            });
            SwitchPreference switch2= (SwitchPreference) findPreference(getString(R.string.pref_auto_logout));
            switch2.setOnPreferenceClickListener(preference -> {
                if (switch2.isChecked()){
                    dis="1";
                    Log.d("ffff","send"+dis);
                }
                if (!switch2.isChecked()){
                    dis="2";
                    Log.d("ffff","send2"+dis);
                }
                return true;
            });
            entries=default_order.getEntries();
            default_order.setSummary(default_order.getEntry());
            default_order.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    index=default_order.findIndexOfValue((String) newValue);
                    default_order.setSummary(entries[index]);
                    if (switch1.isChecked()){
                        switch (index){
                            case 0:
                                String autoConnection="1";
                                Log.d("ffff","default2:"+autoConnection);
                                Messenger.getDefault().send(autoConnection,NetActivity.TOKEN1);
                                break;
                            case 1:
                                String autoConnection1="2";
                                Log.d("ffff","default2:"+autoConnection1);
                                Messenger.getDefault().send(autoConnection1,NetActivity.TOKEN1);
                                break;
                            default:
                                break;
                        }
                    }
                    return true;
                }
            });

        }
    }
}
