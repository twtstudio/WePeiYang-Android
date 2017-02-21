package com.twtstudio.retrox.wepeiyangrd.settings;

import android.os.Bundle;
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

        Button button1 = (Button) findViewById(R.id.btn_bind_library);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TjuLibProvider provider = new TjuLibProvider(SettingsActivity.this);
                provider.bindLibrary(Logger::d,"000000");
            }
        });

        Button button2 = (Button) findViewById(R.id.btn_clear_pref);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonPrefUtil.clearAll();
            }
        });


    }
}
