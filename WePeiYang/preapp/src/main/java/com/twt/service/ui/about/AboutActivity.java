package com.twt.service.ui.about;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.support.ApplicationUtils;
import com.twt.service.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AboutActivity extends BaseActivity {

    @InjectView(R.id.tv_version)
    TextView tvVersion;


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);
        String version = ApplicationUtils.getVersionName();
        tvVersion.setText(version);

    }
}
