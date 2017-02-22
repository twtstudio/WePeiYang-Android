package com.twtstudio.retrox.wepeiyangrd.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
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
                .replace(R.id.settings_container,fragment)
                .commit();

    }

    public static class SettingsFragment extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
            initPrefs();
        }

        public void initPrefs(){
            Preference libPref = findPreference(getString(R.string.pref_is_bind_lib));
            
        }
    }
}
