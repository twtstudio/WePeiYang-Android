package com.rex.wepeiyang.ui.gpa.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.ui.BaseFragment;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class ScoreFragment extends BaseFragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_score, container, false);
    }
}
