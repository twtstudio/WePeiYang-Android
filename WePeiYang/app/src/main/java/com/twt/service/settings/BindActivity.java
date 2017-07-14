package com.twt.service.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;

import agency.tango.materialintroscreen.MaterialIntroActivity;

/**
 * Created by retrox on 01/03/2017.
 */

public class BindActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);
        addSlide(new TjuBindFragment());
        addSlide(new LibBindFragment());

    }
}
