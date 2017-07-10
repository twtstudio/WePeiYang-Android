package com.twt.service.module.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twt.service.home.HomeActivity;
import com.twt.service.settings.LibBindFragment;
import com.twt.service.settings.TjuBindFragment;

import agency.tango.materialintroscreen.MaterialIntroActivity;

/**
 * Created by retrox on 12/03/2017.
 */

public class WelcomeSlideActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(new GpaWelcomeFragment());
        addSlide(new ClassTableWelcomeFragment());
        addSlide(new BikeWelcomeFragment());
        addSlide(new BriefWelcomeFragment());
        boolean isBindTju = CommonPrefUtil.getIsBindTju();
        if (!isBindTju) {
            addSlide(new TjuBindFragment());
        }
        boolean isBindLib = CommonPrefUtil.getIsBindLibrary();
        if (!isBindLib) {
            addSlide(new LibBindFragment());
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
