package com.rex.wepeiyang.ui.gpa.analysis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.Gpa;
import com.rex.wepeiyang.ui.BaseFragment;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class AnalysisFragment extends BaseFragment implements AnalysisView{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analyis_score,container,false);
    }

    @Override
    public void bindData(Gpa gpa) {

    }
}
