package com.rex.wepeiyang.ui.gpa.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.Gpa;
import com.rex.wepeiyang.ui.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class ScoreFragment extends BaseFragment implements ScoreView {
    @InjectView(R.id.line_chart)
    LineChart lineChart;
    @InjectView(R.id.tv_zongjiaquan)
    TextView tvZongjiaquan;
    @InjectView(R.id.tv_zongjidian)
    TextView tvZongjidian;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_score, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void bindData(Gpa gpa) {
        tvZongjiaquan.setText(gpa.data.stat.total.score + "");
        tvZongjidian.setText(gpa.data.stat.total.gpa + "");

    }
}
