package com.twt.service.module.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.R;

import agency.tango.materialintroscreen.SlideFragment;

/**
 * Created by retrox on 12/03/2017.
 */

public class ClassTableWelcomeFragment extends SlideFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classtable_welcome_slide, container, false);
    }

    @Override
    public int backgroundColor() {
        return R.color.white_color;
    }

    @Override
    public int buttonsColor() {
        return R.color.colorAccent;
    }

    @Override
    public boolean canMoveFurther() {
        return true;
    }
}
